<p align="center">
  <img src="assets/ss.png" alt="Open Slinky Banner" width="350" />
  <br>
  <font size="6"><b>Open Slink</b></font>
  <br>
  <font size="3">by Skid Solutions</font>
  <br>
  <font size="2">credits to xmqr0 & hitsconfig</font>
</p>

---

<p align="center">
  <a href="https://discord.gg/AEwatvnvSf">https://discord.gg/AEwatvnvSf</a>
</p>

---

## How to build

### Prerequisites
- **C++ Compiler**: Visual Studio 2019 / 2022 (MSVC with C++17 support)
- **CMake**: Version 3.15 or newer
- **Java Development Kit**:
  - **JDK 17**: Required by Gradle to compile `SlinkClient`
  - **JDK 8**: Runtime environment for Minecraft 1.8.9

---

### 1. Building the Loader 

The loader and native injector DLL are configured as a unified CMake project:

```powershell
cmake -B build -A x64

cmake --build build --config Release
```

Output binaries will be placed in `build/bin/Release/`:


---

### 2. Building the Client (`Slinkclient`)

The client is a standalone Gradle project using [Unimined](https://github.com/unimined/Unimined) to resolve Minecraft 1.8.9 dependencies and MCP mappings automatically:

```powershell
cd SlinkClient

$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"

.\gradlew.bat clean build
```

Generated `SlinkyClient/build/libs/`:
