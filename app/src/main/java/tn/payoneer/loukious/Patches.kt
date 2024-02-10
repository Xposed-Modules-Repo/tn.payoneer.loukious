package tn.payoneer.loukious

import android.annotation.SuppressLint
import android.util.Log
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import org.luckypray.dexkit.DexKitBridge

const val TAG = "PayoneerRootDetectionBypass"

fun removeDetection (cl: ClassLoader, bridge: DexKitBridge) {
    try {
        val classesData = bridge.findClass {
            searchPackages("com.gantix.JailMonkey", "com.scottyab.rootbeer")
            matcher {}
        }
        for (classData in classesData) {
            val loadedClass = classData.getInstance(cl)
            loadedClass.declaredMethods.forEach { method ->
                try {
                    XposedBridge.hookMethod(method, object : XC_MethodHook() {
                        override fun afterHookedMethod(param: MethodHookParam?) {
                            if (param?.result == true) {
                                param.result = false
                            }
                        }
                    })
                } catch (e : Exception){
                    Log.e(TAG, "Failed to replace method ${method.name} in class ${loadedClass.name} with a no-op. Error: ${e.message}")
                }
            }

        }

    } catch (e: Exception) {
        Log.e(TAG, "Failed to create hooks for root detection bypass. Error: ${e.message}")
    }
}
