package model;
import java.util.List;

public class HorarioAtendimento {
    private String nomeDoProfessor;
    private String horarioDeAtendimento;
    private String periodo;
    private int sala;
    private List<Integer> predio;

    public int determinarPredio() {
        return (sala - 1) / 5 + 1;
    }

    public int getSala() { return sala; }
    public void setSala(int sala) { this.sala = sala; }
}