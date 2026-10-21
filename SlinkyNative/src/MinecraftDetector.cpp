#include "MinecraftDetector.hpp"
#include "JvmManager.hpp"
#include "Logger.hpp"

namespace SlinkyNative {

bool MinecraftDetector::Detect(MinecraftInfo& outInfo) {
    JNIEnv* env = JvmManager::GetEnv();
    jvmtiEnv* jvmti = JvmManager::GetJvmti();

    if (!env || !jvmti) return false;

    // Wait up to 2 minutes for Minecraft 1.8.9 to finish starting
    ULONGLONG startTime = GetTickCount64();
    const ULONGLONG timeoutMs = 120000; // 2 minutes

    while (GetTickCount64() - startTime < timeoutMs) {
        jint classCount = 0;
        jclass* classes = nullptr;
        jvmtiError err = jvmti->GetLoadedClasses(&classCount, &classes);

        if (err == JVMTI_ERROR_NONE && classes) {
            jclass candidateMinecraft = nullptr;
            jclass candidateAve = nullptr;

            for (jint i = 0; i < classCount; ++i) {
                char* signature = nullptr;
                char* generic = nullptr;
                if (jvmti->GetClassSignature(classes[i], &signature, &generic) == JVMTI_ERROR_NONE && signature) {
                    if (strcmp(signature, "Lnet/minecraft/client/Minecraft;") == 0) {
                        candidateMinecraft = classes[i];
                    } else if (strcmp(signature, "Lave;") == 0) {
                        candidateAve = classes[i];
                    }
                    jvmti->Deallocate((unsigned char*)signature);
                    if (generic) jvmti->Deallocate((unsigned char*)generic);
                }
            }

            // Check candidateMinecraft (MCP or SRG)
            if (candidateMinecraft) {
                // Check MCP: theMinecraft (Lnet/minecraft/client/Minecraft;)
                jfieldID fId = env->GetStaticFieldID(candidateMinecraft, "theMinecraft", "Lnet/minecraft/client/Minecraft;");
                if (fId) {
                    outInfo.mapping = MappingType::Named;
                    outInfo.mappingName = "named";
                    outInfo.resourceId = 101;
                    outInfo.minecraftClass = (jclass)env->NewGlobalRef(candidateMinecraft);

                    jvmti->GetClassLoader(candidateMinecraft, &outInfo.classLoader);
                    if (outInfo.classLoader) {
                        outInfo.classLoader = env->NewGlobalRef(outInfo.classLoader);
                    }

                    jvmti->Deallocate((unsigned char*)classes);
                    Logger::Log(L"detected Minecraft 1.8.9 with %hs names", outInfo.mappingName.c_str());
                    return true;
                }
                env->ExceptionClear();

                // Check SRG: field_71432_P (Lnet/minecraft/client/Minecraft;)
                fId = env->GetStaticFieldID(candidateMinecraft, "field_71432_P", "Lnet/minecraft/client/Minecraft;");
                if (fId) {
                    outInfo.mapping = MappingType::Srg;
                    outInfo.mappingName = "srg";
                    outInfo.resourceId = 102;
                    outInfo.minecraftClass = (jclass)env->NewGlobalRef(candidateMinecraft);

                    jvmti->GetClassLoader(candidateMinecraft, &outInfo.classLoader);
                    if (outInfo.classLoader) {
                        outInfo.classLoader = env->NewGlobalRef(outInfo.classLoader);
                    }

                    jvmti->Deallocate((unsigned char*)classes);
                    Logger::Log(L"detected Minecraft 1.8.9 with %hs names", outInfo.mappingName.c_str());
                    return true;
                }
                env->ExceptionClear();

                Logger::Log(L"net.minecraft.client.Minecraft has neither MCP nor SRG members; unsupported runtime");
                jvmti->Deallocate((unsigned char*)classes);
                return false;
            }

            // Check candidateAve (Official / Notch)
            if (candidateAve) {
                // Check official: static field S (Lave;)
                jfieldID fId = env->GetStaticFieldID(candidateAve, "S", "Lave;");
                if (fId) {
                    outInfo.mapping = MappingType::Official;
                    outInfo.mappingName = "official";
                    outInfo.resourceId = 103;
                    outInfo.minecraftClass = (jclass)env->NewGlobalRef(candidateAve);

                    jvmti->GetClassLoader(candidateAve, &outInfo.classLoader);
                    if (outInfo.classLoader) {
                        outInfo.classLoader = env->NewGlobalRef(outInfo.classLoader);
                    }

                    jvmti->Deallocate((unsigned char*)classes);
                    Logger::Log(L"detected Minecraft 1.8.9 with %hs names", outInfo.mappingName.c_str());
                    return true;
                }
                env->ExceptionClear();

                Logger::Log(L"class ave is not Minecraft 1.8.9; unsupported runtime");
                jvmti->Deallocate((unsigned char*)classes);
                return false;
            }

            jvmti->Deallocate((unsigned char*)classes);
        }

        Sleep(500);
    }

    Logger::Log(L"Minecraft 1.8.9 did not finish starting within 2 minutes");
    return false;
}

} // namespace SlinkyNative
