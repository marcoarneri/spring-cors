package it.krisopea.springcors.avro;

import org.apache.avro.Schema;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Component
public class AvroSchemaFileWriter {

    public File writeSchemaToFile(Schema schema) throws IOException {
        String filePath = "src/main/resources/avroHttpRequest-utenteSchema.avsc"; // Percorso statico
        File file = new File(filePath);

        if (file.exists()) {
            file.delete();
        }

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(schema.toString());
        }
        return file;
    }
}
