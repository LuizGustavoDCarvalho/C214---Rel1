package br.inatel;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AtendimentoProfessorTest {

    private ServidorRemoto servidorMock;
    private AtendimentoProfessor atendimento;

    @BeforeEach
    public void setUp() {
        servidorMock = mock(ServidorRemoto.class);
        atendimento = new AtendimentoProfessor(servidorMock);
    }

    @Test
    public void testSucessoSala3Predio1() {
        String json = """
            {
              "nomeDoProfessor": "Ana",
              "horarioDeAtendimento": "10:00 - 12:00",
              "periodo": "integral",
              "sala": 3,
              "predio": ["1"]
            }
            """;
        when(servidorMock.getJson()).thenReturn(json);
        String resultado = atendimento.processar();
        assertTrue(resultado.contains("Ana - 10:00 - 12:00 - Sala 3 - Prédio 1"));
    }

    @Test
    public void testFalhaPredioErrado() {
        String json = """
            {
              "nomeDoProfessor": "Carlos",
              "horarioDeAtendimento": "14:00 - 16:00",
              "periodo": "noturno",
              "sala": 6,
              "predio": ["1"]
            }
            """;
        when(servidorMock.getJson()).thenReturn(json);
        assertThrows(IllegalArgumentException.class, () -> atendimento.processar());
    }

    @Test
    public void testSucessoSala1Predio1() {
        String json = """
        {
          "nomeDoProfessor": "Prof1",
          "horarioDeAtendimento": "08:00 - 10:00",
          "periodo": "integral",
          "sala": 1,
          "predio": ["1"]
        }
        """;
        when(servidorMock.getJson()).thenReturn(json);
        String resultado = atendimento.processar();
        assertTrue(resultado.contains("Prof1 - 08:00 - 10:00 - Sala 1 - Prédio 1"));
    }

    @Test
    public void testSucessoSala5Predio1() {
        String json = """
        {
          "nomeDoProfessor": "Prof2",
          "horarioDeAtendimento": "09:00 - 11:00",
          "periodo": "noturno",
          "sala": 5,
          "predio": ["1"]
        }
        """;
        when(servidorMock.getJson()).thenReturn(json);
        String resultado = atendimento.processar();
        assertTrue(resultado.contains("Prof2"));
    }

    @Test
    public void testSucessoSala6Predio2() {
        String json = """
        {
          "nomeDoProfessor": "Prof3",
          "horarioDeAtendimento": "10:00 - 12:00",
          "periodo": "integral",
          "sala": 6,
          "predio": ["2"]
        }
        """;
        when(servidorMock.getJson()).thenReturn(json);
        assertDoesNotThrow(() -> atendimento.processar());
    }

    @Test
    public void testSucessoSala10Predio2() {
        String json = """
        {
          "nomeDoProfessor": "Prof4",
          "horarioDeAtendimento": "11:00 - 13:00",
          "periodo": "noturno",
          "sala": 10,
          "predio": ["2"]
        }
        """;
        when(servidorMock.getJson()).thenReturn(json);
        String resultado = atendimento.processar();
        assertTrue(resultado.contains("Sala 10"));
    }

    @Test
    public void testSucessoSala11Predio3() {
        String json = """
        {
          "nomeDoProfessor": "Prof5",
          "horarioDeAtendimento": "13:00 - 15:00",
          "periodo": "integral",
          "sala": 11,
          "predio": ["3"]
        }
        """;
        when(servidorMock.getJson()).thenReturn(json);
        assertDoesNotThrow(() -> atendimento.processar());
    }

    @Test
    public void testSucessoSala15Predio3() {
        String json = """
        {
          "nomeDoProfessor": "Prof6",
          "horarioDeAtendimento": "14:00 - 16:00",
          "periodo": "noturno",
          "sala": 15,
          "predio": ["3"]
        }
        """;
        when(servidorMock.getJson()).thenReturn(json);
        assertDoesNotThrow(() -> atendimento.processar());
    }

    @Test
    public void testSucessoSala16Predio4() {
        String json = """
        {
          "nomeDoProfessor": "Prof7",
          "horarioDeAtendimento": "15:00 - 17:00",
          "periodo": "integral",
          "sala": 16,
          "predio": ["4"]
        }
        """;
        when(servidorMock.getJson()).thenReturn(json);
        assertDoesNotThrow(() -> atendimento.processar());
    }

    @Test
    public void testSucessoSala20Predio4() {
        String json = """
        {
          "nomeDoProfessor": "Prof8",
          "horarioDeAtendimento": "16:00 - 18:00",
          "periodo": "noturno",
          "sala": 20,
          "predio": ["4"]
        }
        """;
        when(servidorMock.getJson()).thenReturn(json);
        assertDoesNotThrow(() -> atendimento.processar());
    }

    @Test
    public void testSucessoMultiplosPrediosIncluiValido() {
        String json = """
        {
          "nomeDoProfessor": "Prof9",
          "horarioDeAtendimento": "08:00 - 10:00",
          "periodo": "integral",
          "sala": 3,
          "predio": ["2", "3", "1"]
        }
        """;
        when(servidorMock.getJson()).thenReturn(json);
        assertDoesNotThrow(() -> atendimento.processar());
    }

    @Test
    public void testSucessoMultiplosPrediosUltimoValido() {
        String json = """
        {
          "nomeDoProfessor": "Prof10",
          "horarioDeAtendimento": "09:00 - 11:00",
          "periodo": "integral",
          "sala": 12,
          "predio": ["1", "2", "3"]
        }
        """;
        when(servidorMock.getJson()).thenReturn(json);
        assertDoesNotThrow(() -> atendimento.processar());
    }

    @Test
    public void testFalhaPredioErradoSala1() {
        String json = """
        {
          "nomeDoProfessor": "Erro1",
          "horarioDeAtendimento": "08:00 - 10:00",
          "periodo": "integral",
          "sala": 1,
          "predio": ["2"]
        }
        """;
        when(servidorMock.getJson()).thenReturn(json);
        assertThrows(IllegalArgumentException.class, () -> atendimento.processar());
    }

    @Test
    public void testFalhaPredioErradoSala6() {
        String json = """
        {
          "nomeDoProfessor": "Erro2",
          "horarioDeAtendimento": "09:00 - 11:00",
          "periodo": "noturno",
          "sala": 6,
          "predio": ["1"]
        }
        """;
        when(servidorMock.getJson()).thenReturn(json);
        assertThrows(IllegalArgumentException.class, () -> atendimento.processar());
    }

    @Test
    public void testFalhaPredioErradoSala11() {
        String json = """
        {
          "nomeDoProfessor": "Erro3",
          "horarioDeAtendimento": "10:00 - 12:00",
          "periodo": "integral",
          "sala": 11,
          "predio": ["2"]
        }
        """;
        when(servidorMock.getJson()).thenReturn(json);
        assertThrows(IllegalArgumentException.class, () -> atendimento.processar());
    }

    @Test
    public void testFalhaPredioErradoSala16() {
        String json = """
        {
          "nomeDoProfessor": "Erro4",
          "horarioDeAtendimento": "11:00 - 13:00",
          "periodo": "integral",
          "sala": 16,
          "predio": ["1", "2", "3"]
        }
        """;
        when(servidorMock.getJson()).thenReturn(json);
        assertThrows(IllegalArgumentException.class, () -> atendimento.processar());
    }

    @Test
    public void testFalhaSemPredios() {
        String json = """
        {
          "nomeDoProfessor": "Erro5",
          "horarioDeAtendimento": "12:00 - 14:00",
          "periodo": "integral",
          "sala": 4,
          "predio": []
        }
        """;
        when(servidorMock.getJson()).thenReturn(json);
        assertThrows(IllegalArgumentException.class, () -> atendimento.processar());
    }

    @Test
    public void testFalhaSalaNegativa() {
        String json = """
        {
          "nomeDoProfessor": "Erro6",
          "horarioDeAtendimento": "13:00 - 15:00",
          "periodo": "integral",
          "sala": -1,
          "predio": ["1"]
        }
        """;
        when(servidorMock.getJson()).thenReturn(json);
        assertThrows(IllegalArgumentException.class, () -> atendimento.processar());
    }

    @Test
    public void testFalhaSalaZero() {
        String json = """
        {
          "nomeDoProfessor": "Erro7",
          "horarioDeAtendimento": "14:00 - 16:00",
          "periodo": "integral",
          "sala": 0,
          "predio": ["1"]
        }
        """;
        when(servidorMock.getJson()).thenReturn(json);
        assertThrows(IllegalArgumentException.class, () -> atendimento.processar());
    }

    @Test
    public void testFalhaSemSala() {
        String json = """
        {
          "nomeDoProfessor": "Erro8",
          "horarioDeAtendimento": "15:00 - 17:00",
          "periodo": "integral",
          "predio": ["1"]
        }
        """;
        when(servidorMock.getJson()).thenReturn(json);
        assertThrows(NullPointerException.class, () -> atendimento.processar());
    }

    @Test
    public void testFalhaSemPredioCampo() {
        String json = """
        {
          "nomeDoProfessor": "Erro9",
          "horarioDeAtendimento": "16:00 - 18:00",
          "periodo": "noturno",
          "sala": 3
        }
        """;
        when(servidorMock.getJson()).thenReturn(json);
        assertThrows(NullPointerException.class, () -> atendimento.processar());
    }

    @Test
    public void testFalhaJSONMalformado() {
        String json = "{ sala: 3, predio: [1] "; // JSON malformado
        when(servidorMock.getJson()).thenReturn(json);
        assertThrows(Exception.class, () -> atendimento.processar());
    }


}

