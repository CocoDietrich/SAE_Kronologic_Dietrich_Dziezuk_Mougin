package kronologic.outils;

import javafx.application.Platform;

public class InitialiseurJavaFX {

    private static boolean toolkitInitialized = false;

    public static void initToolkit() {
        if (!toolkitInitialized) {
            Platform.startup(() -> {});
            toolkitInitialized = true;
        }
    }
}
