#include <windows.h>
#include "ConsoleUI.hpp"
#include "Injector.hpp"
#include "ProcessUtil.hpp"
#include <cstdio>
#include <vector>

using namespace Slinky;

int main() {
    ConsoleUI::Initialize();
    ConsoleUI::StartAnimation();

    ConsoleUI::SetStatusText("Finding bundled DLL");
    std::wstring dllPath;
    std::wstring logPath;

    if (!Injector::ExtractBundledDll(dllPath, logPath)) {
        Sleep(2500);
        ConsoleUI::StopAnimation();
        ConsoleUI::Shutdown();
        return 1;
    }

    ConsoleUI::SetStatusText("SCANNING");

    DWORD startWait = GetTickCount();
    const DWORD timeoutWait = 180000;
    std::vector<TargetWindow> windows;
    int dotCount = 0;

    while (GetTickCount() - startWait < timeoutWait) {
        windows = ProcessUtil::FindMinecraftWindows();
        if (!windows.empty()) {
            break;
        }

        const char* dots[] = { "", ".", "..", "..." };
        char waitMsg[64];
        sprintf_s(waitMsg, "Waiting for Minecraft%s", dots[dotCount % 4]);
        ConsoleUI::SetStatusText(waitMsg);
        dotCount++;

        Sleep(500);
    }

    if (windows.empty()) {
        ConsoleUI::SetStatusText("Minecraft not found after 3 minutes.");
        Sleep(3000);
        ConsoleUI::StopAnimation();
        ConsoleUI::Shutdown();
        return 1;
    }

    TargetWindow target = windows[0];

    char foundMsg[256];
    char titleUtf8[256] = { 0 };
    WideCharToMultiByte(CP_UTF8, 0, target.title.c_str(), -1, titleUtf8, 256, nullptr, nullptr);
    sprintf_s(foundMsg, "Found Minecraft %s", titleUtf8);
    ConsoleUI::SetStatusText(foundMsg);

    Sleep(500);

    ConsoleUI::SetStatusText("INJECTING ...");
    if (!Injector::InjectDll(target.pid, dllPath)) {
        Sleep(3000);
        ConsoleUI::StopAnimation();
        ConsoleUI::Shutdown();
        return 1;
    }

    bool success = Injector::MonitorStartup(target.pid, logPath);

    Sleep(3000);
    ConsoleUI::StopAnimation();
    ConsoleUI::Shutdown();

    return success ? 0 : 1;
}
