#include "Logger.hpp"
#include <cstdio>
#include <vector>

namespace SlinkyNative {

CRITICAL_SECTION Logger::s_CriticalSection;
std::wstring Logger::s_LogFilePath;
bool Logger::s_Initialized = false;

void Logger::Initialize(HMODULE hModule) {
    if (s_Initialized) return;
    InitializeCriticalSection(&s_CriticalSection);

    wchar_t modulePath[MAX_PATH] = { 0 };
    if (GetModuleFileNameW(hModule, modulePath, MAX_PATH) > 0) {
        wchar_t* lastSlash = wcsrchr(modulePath, L'\\');
        if (lastSlash) {
            *lastSlash = L'\0';
            s_LogFilePath = std::wstring(modulePath) + L"\\slinky-native.log";
        }
    }

    if (s_LogFilePath.empty()) {
        wchar_t tempPath[MAX_PATH] = { 0 };
        GetTempPathW(MAX_PATH, tempPath);
        s_LogFilePath = std::wstring(tempPath) + L"slinky-native.log";
    }

    s_Initialized = true;
}

void Logger::Shutdown() {
    if (!s_Initialized) return;
    DeleteCriticalSection(&s_CriticalSection);
    s_Initialized = false;
}

const std::wstring& Logger::GetLogFilePath() {
    return s_LogFilePath;
}

void Logger::Log(const wchar_t* format, ...) {
    va_list args;
    va_start(args, format);
    LogV(format, args);
    va_end(args);
}

void Logger::LogV(const wchar_t* format, va_list args) {
    if (!s_Initialized) return;

    SYSTEMTIME st;
    GetLocalTime(&st);

    wchar_t messageBuf[2048] = { 0 };
    vswprintf_s(messageBuf, format, args);

    wchar_t lineBuf[2560] = { 0 };
    swprintf_s(lineBuf, L"[%04u-%02u-%02u %02u:%02u:%02u.%03u] %ls\n",
        st.wYear, st.wMonth, st.wDay,
        st.wHour, st.wMinute, st.wSecond, st.wMilliseconds,
        messageBuf);

    EnterCriticalSection(&s_CriticalSection);

    // Also output to debug string for debugger attach
    OutputDebugStringW(lineBuf);

    // Write to slinky-native.log
    FILE* fp = nullptr;
    if (_wfopen_s(&fp, s_LogFilePath.c_str(), L"a, ccs=UTF-8") == 0 && fp) {
        fputws(lineBuf, fp);
        fclose(fp);
    }

    LeaveCriticalSection(&s_CriticalSection);
}

void Logger::LogA(const char* format, ...) {
    va_list args;
    va_start(args, format);

    char charBuf[2048] = { 0 };
    vsprintf_s(charBuf, format, args);
    va_end(args);

    wchar_t wideBuf[2048] = { 0 };
    MultiByteToWideChar(CP_UTF8, 0, charBuf, -1, wideBuf, 2048);
    Log(L"%ls", wideBuf);
}

} // namespace SlinkyNative
