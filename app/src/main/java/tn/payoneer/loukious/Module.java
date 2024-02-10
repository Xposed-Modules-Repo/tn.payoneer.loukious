package tn.payoneer.loukious;

import android.util.Log;

import org.luckypray.dexkit.DexKitBridge;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public final class Module implements IXposedHookLoadPackage {

    private static final String TAG = "PayoneerRootDetectionBypass";

    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

        var cl = lpparam.classLoader;
        System.loadLibrary("dexkit");
        try (DexKitBridge bridge = DexKitBridge.create(lpparam.appInfo.sourceDir)) {
            if (bridge == null) {
                Log.e(TAG, "Failed to create DexKitBridge");
                return;
            }

            PatchesKt.removeDetection(cl, bridge);
        } catch (Exception e) {
            Log.e(TAG, "Failed to find method", e);
        }
    }

}
