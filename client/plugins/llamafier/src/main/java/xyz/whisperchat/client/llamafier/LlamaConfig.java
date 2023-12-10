package xyz.whisperchat.client.llamafier;

class LlamaConfig {
    // Format that the LLM returns its message in
    // The expected response is { "rewordedMessage": "message" }
    // I know that sounds weird for a single message but it makes the output
    // a lot more predictable
    public static String LLAMA_GRAMMAR = "root ::= Response\n" + //
        "Response ::= \"{\"   ws   \"\\\"rewordedMessage\\\":\"   ws   string   \"}\"\n" + //
        "Responselist ::= \"[]\" | \"[\"   ws   Response   (\",\"   ws   Response)*   \"]\"\n" + //
        "string ::= \"\\\"\"   ([^\"]*)   \"\\\"\"\n" + //
        "boolean ::= \"true\" | \"false\"\n" + //
        "ws ::= [ \\t\\n" + //
        "]*\n" + //
        "number ::= [0-9]+   \".\"?   [0-9]*\n" + //
        "stringlist ::= \"[\"   ws   \"]\" | \"[\"   ws   string   (\",\"   ws   string)*   ws   \"]\"\n" + //
        "numberlist ::= \"[\"   ws   \"]\" | \"[\"   ws   string   (\",\"   ws   number)*   ws   \"]\"\n";

    // The prompt itself
    // Append user message and send
    public static String LLAMA_PROMPT = "The AI system is tasked with rewording user messages in a " +
        "succinct and neutral style while preserving the core meaning and intent of the original message." +
        " This involves altering the structure, word choice, and style of a message.\n" +
        "User: ";
    
    public static String genPrompt(String userInput) {
        return LLAMA_PROMPT + userInput + "\nAI: ";
    }
}
