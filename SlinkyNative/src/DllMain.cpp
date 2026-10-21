#include <windows.h>
#include "Logger.hpp"
#include "JvmManager.hpp"
#include "MinecraftDetector.hpp"
#include "PayloadLoader.hpp"

static HMODULE g_hModule = nullptr;

static DWORD RunWorker(HMODULE hModule) {
    if (!SlinkyNative::JvmManager::Initialize()) {
        SlinkyNative::Logger::Log(L"FINAL STATUS: FAILED");
        return 1;
    }

    SlinkyNative::MinecraftInfo mcInfo;
    if (!SlinkyNative::MinecraftDetector::Detect(mcInfo)) {
        SlinkyNative::Logger::Log(L"FINAL STATUS: FAILED");
        return 1;
    }

    SlinkyNative::InjectionStatus status = SlinkyNative::PayloadLoader::LoadAndStart(hModule, mcInfo);

    return (status == SlinkyNative::InjectionStatus::Success || status == SlinkyNative::InjectionStatus::AlreadyRunning) ? 0 : 1;
}

DWORD WINAPI SlinkyNativeWorkerThread(LPVOID lpParam) {
    HMODULE hModule = (HMODULE)lpParam;
    SlinkyNative::Logger::Initialize(hModule);

    DWORD ret = 1;
    __try {
        ret = RunWorker(hModule);
    }
    __except (EXCEPTION_EXECUTE_HANDLER) {
        SlinkyNative::Logger::Log(L"SlinkyNativeWorkerThread crashed with exception 0x%08X", GetExceptionCode());
        SlinkyNative::Logger::Log(L"FINAL STATUS: FAILED");
        ret = 1;
    }
    return ret;
}

BOOL WINAPI DllMain(HINSTANCE hinstDLL, DWORD fdwReason, LPVOID lpReserved) {
    switch (fdwReason) {
    case DLL_PROCESS_ATTACH:
        g_hModule = (HMODULE)hinstDLL;
        DisableThreadLibraryCalls(hinstDLL);
        CreateThread(nullptr, 0, SlinkyNativeWorkerThread, (LPVOID)hinstDLL, 0, nullptr);
        break;
    case DLL_PROCESS_DETACH:
        SlinkyNative::JvmManager::Shutdown();
        SlinkyNative::Logger::Shutdown();
        break;
    }
    return TRUE;
}
