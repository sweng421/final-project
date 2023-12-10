package xyz.whisperchat.client.llamafier;

import org.pf4j.Extension;
import org.pf4j.Plugin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import de.kherud.llama.InferenceParameters;
import de.kherud.llama.LlamaModel;
import de.kherud.llama.ModelParameters;
import xyz.whisperchat.client.plugin.ExtensionUtil;
import xyz.whisperchat.client.plugin.StylometricAnonymizer;

public class LlamaAnonPlugin extends Plugin {
    public LlamaAnonPlugin() {
        super();
    }

    @Override
    public void start() {
        System.out.println("Starting LLAMA plugin");
    }

    @Override
    public void stop() {
        System.out.println("Stopping LLAMA plugin");
    }

    @Extension(ordinal=1)
    public static class LlamaAnonExt implements StylometricAnonymizer {
        private LlamaModel model = null;
        private InferenceParameters inferParams;
        private ObjectReader jsonReader = new ObjectMapper().readerFor(GrammarFormat.class);

        @Override
        public String anonymize(String message) throws Exception {
            StringBuilder response = new StringBuilder();
            for (LlamaModel.Output output : model.generate(LlamaConfig.genPrompt(message), inferParams)) {
                response.append(output);
            }

            GrammarFormat parsedResponse = jsonReader.readValue(response.toString(), GrammarFormat.class);
            return parsedResponse.getRewordedMessage();
        }

        @Override
        public void setup(ExtensionUtil util) throws Exception {
            ModelParameters params = new ModelParameters.Builder()
                .setNGpuLayers(43)
                .build();
            inferParams = new InferenceParameters.Builder()
                .setGrammar(LlamaConfig.LLAMA_GRAMMAR)
                .build();
            String modelLocation = util.fileDialog("GGUF model file", "gguf").getAbsolutePath();
            model = new LlamaModel(modelLocation, params);
        }

        @Override
        public void close() {
            if (model != null) {
                model.close();
            }
        }
    }
}
