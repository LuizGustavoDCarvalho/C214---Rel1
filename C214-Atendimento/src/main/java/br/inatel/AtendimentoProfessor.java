package br.inatel;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class AtendimentoProfessor {

    private ServidorRemoto servidor;

    public AtendimentoProfessor(ServidorRemoto servidor) {
        this.servidor = servidor;
    }

    public String processar() {
        String jsonString = servidor.getJson();
        JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();

        int sala = json.get("sala").getAsInt();
        String nome = json.get("nomeDoProfessor").getAsString();
        String horario = json.get("horarioDeAtendimento").getAsString();

        String predioEsperado;

        if (sala >= 1 && sala <= 5)
            predioEsperado = "1";
        else if (sala <= 10)
            predioEsperado = "2";
        else if (sala <= 15)
            predioEsperado = "3";
        else
            predioEsperado = "4";

        boolean predioValido = false;
        for (var predio : json.getAsJsonArray("predio")) {
            if (predio.getAsString().equals(predioEsperado)) {
                predioValido = true;
                break;
            }
        }

        if (!predioValido) {
            throw new IllegalArgumentException("Predio incorreto para a sala: " + sala);
        }

        return nome + " - " + horario + " - Sala " + sala + " - Prédio " + predioEsperado;
    }
}
