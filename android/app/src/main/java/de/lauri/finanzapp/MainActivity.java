package de.lauri.finanzapp;

import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import com.getcapacitor.BridgeActivity;
import java.util.concurrent.Executor;

public class MainActivity extends BridgeActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WebView webView = this.bridge.getWebView();
        if (webView != null) {
            webView.addJavascriptInterface(new AndroidBiometricInterface(this, webView), "AndroidBiometrics");
        }
    }

    public static class AndroidBiometricInterface {
        private final MainActivity activity;
        private final WebView webView;

        public AndroidBiometricInterface(MainActivity activity, WebView webView) {
            this.activity = activity;
            this.webView = webView;
        }

        @JavascriptInterface
        public boolean isAvailable() {
            try {
                BiometricManager biometricManager = BiometricManager.from(activity);
                int canAuth = biometricManager.canAuthenticate(
                    BiometricManager.Authenticators.BIOMETRIC_STRONG | BiometricManager.Authenticators.BIOMETRIC_WEAK
                );
                return canAuth == BiometricManager.BIOMETRIC_SUCCESS;
            } catch (Exception e) {
                return false;
            }
        }

        @JavascriptInterface
        public void authenticate(final String title, final String subtitle) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Executor executor = ContextCompat.getMainExecutor(activity);
                        BiometricPrompt biometricPrompt = new BiometricPrompt(activity, executor, new BiometricPrompt.AuthenticationCallback() {
                            @Override
                            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                                super.onAuthenticationError(errorCode, errString);
                                runJs(String.format("window.onAndroidBiometricError && window.onAndroidBiometricError(%d, '%s');",
                                    errorCode, escapeJs(errString.toString())));
                            }

                            @Override
                            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                                super.onAuthenticationSucceeded(result);
                                runJs("window.onAndroidBiometricSuccess && window.onAndroidBiometricSuccess();");
                            }

                            @Override
                            public void onAuthenticationFailed() {
                                super.onAuthenticationFailed();
                                runJs("window.onAndroidBiometricFailed && window.onAndroidBiometricFailed();");
                            }
                        });

                        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                            .setTitle(title != null && !title.isEmpty() ? title : "Haushaltsbuch Barrierefrei")
                            .setSubtitle(subtitle != null && !subtitle.isEmpty() ? subtitle : "Mit Fingerabdruck entsperren")
                            .setNegativeButtonText("Abbrechen")
                            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG | BiometricManager.Authenticators.BIOMETRIC_WEAK)
                            .build();

                        biometricPrompt.authenticate(promptInfo);
                    } catch (Exception e) {
                        runJs("window.onAndroidBiometricError && window.onAndroidBiometricError(-1, '" + escapeJs(e.getMessage()) + "');");
                    }
                }
            });
        }

        private void runJs(final String js) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (webView != null) {
                        webView.evaluateJavascript(js, null);
                    }
                }
            });
        }

        private String escapeJs(String str) {
            if (str == null) return "";
            return str.replace("\\", "\\\\").replace("'", "\\'").replace("\n", " ").replace("\r", "");
        }
    }
}
