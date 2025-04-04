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
        if (sala < 1) return -1; // Caso de erro para salas inválidas
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
        if (json == null || json.trim().isEmpty()) {
            throw new IllegalArgumentException("JSON recebido é inválido ou vazio");
        }
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

    // Casos de sucesso
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

    @Test
    void testObterHorario_SalaNoLimite() {
        String json = "{\"nomeDoProfessor\":\"Prof. Teste\",\"horarioDeAtendimento\":\"09h-11h\",\"periodo\":\"integral\",\"sala\":10,\"predio\":[2]}";
        Mockito.when(remoteServerMock.getHorarioAtendimento()).thenReturn(json);
        HorarioAtendimento horario = service.obterHorario();
        assertEquals(2, horario.determinarPredio());
    }

    @Test
    void testObterHorario_SalaInicioProximoPredio() {
        String json = "{\"nomeDoProfessor\":\"Prof. Novaes\",\"horarioDeAtendimento\":\"13h-14h\",\"periodo\":\"matutino\",\"sala\":11,\"predio\":[3]}";
        Mockito.when(remoteServerMock.getHorarioAtendimento()).thenReturn(json);
        HorarioAtendimento horario = service.obterHorario();
        assertEquals(3, horario.determinarPredio());
    }

    @Test
    void testObterHorario_SalaNoUltimoPredio() {
        String json = "{\"nomeDoProfessor\":\"Dr. Final\",\"horarioDeAtendimento\":\"20h-21h\",\"periodo\":\"noturno\",\"sala\":100,\"predio\":[20]}";
        Mockito.when(remoteServerMock.getHorarioAtendimento()).thenReturn(json);
        HorarioAtendimento horario = service.obterHorario();
        assertEquals(20, horario.determinarPredio());
    }

    @Test
    void testObterHorario_SalaIntermediaria() {
        String json = "{\"nomeDoProfessor\":\"Prof. Inter\",\"horarioDeAtendimento\":\"11h-13h\",\"periodo\":\"vespertino\",\"sala\":25,\"predio\":[5]}";
        Mockito.when(remoteServerMock.getHorarioAtendimento()).thenReturn(json);
        HorarioAtendimento horario = service.obterHorario();
        assertEquals(5, horario.determinarPredio());
    }

    @Test
    void testObterHorario_SalaAleatoria() {
        String json = "{\"nomeDoProfessor\":\"Prof. Ale\",\"horarioDeAtendimento\":\"10h-11h\",\"periodo\":\"integral\",\"sala\":47,\"predio\":[10]}";
        Mockito.when(remoteServerMock.getHorarioAtendimento()).thenReturn(json);
        HorarioAtendimento horario = service.obterHorario();
        assertEquals(10, horario.determinarPredio());
    }

    @Test
    void testObterHorario_SalaCorretaComPredioCorreto() {
        String json = "{\"nomeDoProfessor\":\"Prof. Corret\",\"horarioDeAtendimento\":\"16h-18h\",\"periodo\":\"vespertino\",\"sala\":60,\"predio\":[12]}";
        Mockito.when(remoteServerMock.getHorarioAtendimento()).thenReturn(json);
        HorarioAtendimento horario = service.obterHorario();
        assertEquals(12, horario.determinarPredio());
    }

    // Casos de falha
    @Test
    void testObterHorario_FalhaJsonInvalido() {
        Mockito.when(remoteServerMock.getHorarioAtendimento()).thenReturn("{invalido}");
        assertThrows(Exception.class, () -> {
            service.obterHorario();
        });
    }

    @Test
    void testObterHorario_JsonVazio() {
        Mockito.when(remoteServerMock.getHorarioAtendimento()).thenReturn("");
        assertThrows(IllegalArgumentException.class, () -> {
            service.obterHorario();
        });
    }

    @Test
    void testObterHorario_JsonNulo() {
        Mockito.when(remoteServerMock.getHorarioAtendimento()).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> {
            service.obterHorario();
        });
    }

    @Test
    void testObterHorario_SalaNegativa() {
        String json = "{\"nomeDoProfessor\":\"Dr. Cardoso\",\"horarioDeAtendimento\":\"13h-15h\",\"periodo\":\"vespertino\",\"sala\":-5,\"predio\":[0]}";
        Mockito.when(remoteServerMock.getHorarioAtendimento()).thenReturn(json);
        HorarioAtendimento horario = service.obterHorario();
        assertNotNull(horario);
        assertEquals(-1, horario.determinarPredio());
    }

    @Test
    void testObterHorario_SalaZero() {
        String json = "{\"nomeDoProfessor\":\"Prof. Zero\",\"horarioDeAtendimento\":\"15h-17h\",\"periodo\":\"integral\",\"sala\":0,\"predio\":[0]}";
        Mockito.when(remoteServerMock.getHorarioAtendimento()).thenReturn(json);
        HorarioAtendimento horario = service.obterHorario();
        assertEquals(-1, horario.determinarPredio());
    }

    @Test
    void testObterHorario_SalaExtremamenteAlta() {
        String json = "{\"nomeDoProfessor\":\"Prof. Extremo\",\"horarioDeAtendimento\":\"17h-18h\",\"periodo\":\"noturno\",\"sala\":1000,\"predio\":[200]}";
        Mockito.when(remoteServerMock.getHorarioAtendimento()).thenReturn(json);
        HorarioAtendimento horario = service.obterHorario();
        assertEquals(200, horario.determinarPredio());
    }

    @Test
    void testObterHorario_FalhaJsonSemSala() {
        String json = "{\"nomeDoProfessor\":\"Prof. Incompleto\",\"horarioDeAtendimento\":\"12h-13h\",\"periodo\":\"integral"}"
;
        Mockito.when(remoteServerMock.getHorarioAtendimento()).thenReturn(json);
        HorarioAtendimento horario = service.obterHorario();
        assertEquals(0, horario.getSala());
        assertEquals(-1, horario.determinarPredio());
    }

    @Test
    void testObterHorario_SalaComLetra() {
        String json = "{\"nomeDoProfessor\":\"Prof. ErroTipo\",\"horarioDeAtendimento\":\"10h-11h\",\"periodo\":\"integral\",\"sala\":\"A1\",\"predio\":[0]}";
        Mockito.when(remoteServerMock.getHorarioAtendimento()).thenReturn(json);
        assertThrows(Exception.class, () -> {
            service.obterHorario();
        });
    }

    @Test
    void testObterHorario_JsonFaltandoChavePredio() {
        String json = "{\"nomeDoProfessor\":\"Prof. SemPredio\",\"horarioDeAtendimento\":\"11h-12h\",\"periodo\":\"matutino\",\"sala\":5}";
        Mockito.when(remoteServerMock.getHorarioAtendimento()).thenReturn(json);
        HorarioAtendimento horario = service.obterHorario();
        assertEquals(1, horario.determinarPredio());
    }

    @Test
    void testObterHorario_PredioInconsistenteComSala() {
        String json = "{\"nomeDoProfessor\":\"Prof. Inconsistente\",\"horarioDeAtendimento\":\"13h-15h\",\"periodo\":\"vespertino\",\"sala\":12,\"predio\":[1]}";
        Mockito.when(remoteServerMock.getHorarioAtendimento()).thenReturn(json);
        HorarioAtendimento horario = service.obterHorario();
        assertEquals(3, horario.determinarPredio());
    }
}
