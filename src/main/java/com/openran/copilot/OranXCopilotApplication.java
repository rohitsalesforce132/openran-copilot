package com.openran.copilot;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class OranXCopilotApplication implements QuarkusApplication {

    @Override
    public int run(String... args) throws Exception {
        System.out.println("========================================");
        System.out.println("  OpenRAN Copilot Starting");
        System.out.println("  RAG-powered AI Assistant for O-RAN");
        System.out.println("========================================");
        Quarkus.waitForExit();
        return 0;
    }
}
