package service;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;
import repository.RemoteServer;
import model.HorarioAtendimento;

class HorarioAtendimentoServiceTest {
    private RemoteServer remoteServerMock;
    private HorarioAtendimentoService service;

    @BeforeEach
    void setUp() {
        remoteServerMock = Mockito.mock(RemoteServer.class);
        service = new HorarioAtendimentoService(remoteServerMock);
    }

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
    void testObterHorario_FalhaJsonInvalido() {
        Mockito.when(remoteServerMock.getHorarioAtendimento()).thenReturn("{invalido}");

        assertThrows(Exception.class, () -> {
            service.obterHorario();
        });
    }
}