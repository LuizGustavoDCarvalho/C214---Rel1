package service;
import com.google.gson.Gson;
import repository.RemoteServer;
import model.HorarioAtendimento;

public class HorarioAtendimentoService {
    private RemoteServer remoteServer;
    private Gson gson = new Gson();

    public HorarioAtendimentoService(RemoteServer remoteServer) {
        this.remoteServer = remoteServer;
    }

    public HorarioAtendimento obterHorario() {
        String json = remoteServer.getHorarioAtendimento();
        return gson.fromJson(json, HorarioAtendimento.class);
    }
}