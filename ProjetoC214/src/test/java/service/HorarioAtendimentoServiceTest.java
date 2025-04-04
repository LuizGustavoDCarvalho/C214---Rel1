import com.google.gson.Gson;
import java.util.List;

// Classe que representa o modelo de dados recebido do servidor
class HorarioAtendimento {
    private String nomeDoProfessor;
    private String horarioDeAtendimento;
    private String periodo;
    private int sala;
    private List<Integer> predio;

    public int determinarPredio() {
        return (sala - 1) / 5 + 1;
    }

    // Getters e Setters
    public int getSala() { return sala; }
    public void setSala(int sala) { this.sala = sala; }
}

// Classe responsável por buscar os dados do servidor
class HorarioAtendimentoService {
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

// Interface que simula o servidor remoto
interface RemoteServer {
    String getHorarioAtendimento();
}

// Testes unitários
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;

class HorarioAtendimentoServiceTest {
    private RemoteServer remoteServerMock;
    private HorarioAtendimentoService service;

    @BeforeEach
    void setUp() {
        remoteServerMock = Mockito.mock(RemoteServer.class);
        service = new HorarioAtendimentoService(remoteServerMock);
    }

    // 10 casos de sucesso
    @Test
    void testObterHorario_Sucesso() {
        String json = "{\"nomeDoProfessor\":\"Dr. Silva\",\"horarioDeAtendimento\":\"10h-12h\",\"periodo\":\"integral\",\"sala\":3,\"predio\":[1]}";
        Mockito.when(remoteServerMock.getHorarioAtendimento()).thenReturn(json);
        
        HorarioAtendimento horario = service.obterHorario();
        assertNotNull(horario);
        assertEquals(3, horario.getSala());
        assertEquals(1, horario.determinarPredio());
    }

    @Test
    void testObterHorario_SalaMaxima() {
        String json = "{\"nomeDoProfessor\":\"Dr. Souza\",\"horarioDeAtendimento\":\"14h-16h\",\"periodo\":\"noturno\",\"sala\":99,\"predio\":[20]}";
        Mockito.when(remoteServerMock.getHorarioAtendimento()).thenReturn(json);

        HorarioAtendimento horario = service.obterHorario();
        assertNotNull(horario);
        assertEquals(99, horario.getSala());
        assertEquals(20, horario.determinarPredio());
    }

    @Test
    void testObterHorario_SalaMinima() {
        String json = "{\"nomeDoProfessor\":\"Dra. Lima\",\"horarioDeAtendimento\":\"8h-10h\",\"periodo\":\"matutino\",\"sala\":1,\"predio\":[1]}";
        Mockito.when(remoteServerMock.getHorarioAtendimento()).thenReturn(json);

        HorarioAtendimento horario = service.obterHorario();
        assertNotNull(horario);
        assertEquals(1, horario.getSala());
        assertEquals(1, horario.determinarPredio());
    }

    @Test
    void testObterHorario_PeriodoNoturno() {
        String json = "{\"nomeDoProfessor\":\"Dr. Xavier\",\"horarioDeAtendimento\":\"18h-20h\",\"periodo\":\"noturno\",\"sala\":15,\"predio\":[3]}";
        Mockito.when(remoteServerMock.getHorarioAtendimento()).thenReturn(json);

        HorarioAtendimento horario = service.obterHorario();
        assertNotNull(horario);
        assertEquals("noturno", horario.periodo);
    }

    // 10 casos de falha
    @Test
    void testObterHorario_FalhaJsonInvalido() {
        Mockito.when(remoteServerMock.getHorarioAtendimento()).thenReturn("{invalido}");
        
        assertThrows(Exception.class, () -> {
            service.obterHorario();
        });
    }

    @Test
    void testObterHorario_JsonVazio() {
        Mockito.when(remoteServerMock.getHorarioAtendimento()).thenReturn("{}");
        
        HorarioAtendimento horario = service.obterHorario();
        assertNotNull(horario);
    }

    @Test
    void testObterHorario_JsonNulo() {
        Mockito.when(remoteServerMock.getHorarioAtendimento()).thenReturn(null);
        
        assertThrows(NullPointerException.class, () -> {
            service.obterHorario();
        });
    }

    @Test
    void testObterHorario_SalaNegativa() {
        String json = "{\"nomeDoProfessor\":\"Dr. Cardoso\",\"horarioDeAtendimento\":\"13h-15h\",\"periodo\":\"vespertino\",\"sala\":-5,\"predio\":[0]}";
        Mockito.when(remoteServerMock.getHorarioAtendimento()).thenReturn(json);
        
        HorarioAtendimento horario = service.obterHorario();
        assertNotNull(horario);
        assertTrue(horario.getSala() < 0);
    }
}
