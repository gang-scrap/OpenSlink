#pragma once

#include <windows.h>
#include <string>
#include <atomic>

namespace Slinky {

class ConsoleUI {
public:
    static void Initialize();
    static void Shutdown();

    static void PrintBanner();
    static void StartAnimation();
    static void StopAnimation();

    static void SetStatusText(const char* text);
    static void PrintError(const char* action, DWORD errorCode);

private:
    static DWORD WINAPI AnimationThread(LPVOID lpParam);

    static CRITICAL_SECTION s_CriticalSection;
    static char s_StatusText[512];
    static std::atomic<bool> s_Running;
    static HANDLE s_hAnimationThread;
    static DWORD s_OriginalConsoleMode;
    static HANDLE s_hConsole;
};

} // namespace Slinky
