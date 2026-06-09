package br.com.vacinaja.model;

import jakarta.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Entity
public class PostoSaude {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nome;
    private String cep;
    private String endereco;
    private String regiao;
    private String horarioFuncionamento;
    private String telefone;
    private String diasFuncionamento;
    private LocalTime horaAbertura;
    private LocalTime horaFechamento;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "posto_saude_vacinas_disponiveis",
        joinColumns = @JoinColumn(name = "posto_saude_id"),
        inverseJoinColumns = @JoinColumn(name = "vacinas_disponiveis_id")
    )
    private List<Vacina> vacinasDisponiveis = new ArrayList<>();

    public PostoSaude() {}

    public PostoSaude(String nome, String endereco, String regiao, String horarioFuncionamento, String telefone) {
        this.nome = nome;
        this.endereco = endereco;
        this.regiao = regiao;
        this.horarioFuncionamento = horarioFuncionamento;
        this.telefone = telefone;
        this.diasFuncionamento = "MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY";
        this.horaAbertura = LocalTime.of(7, 0);
        this.horaFechamento = LocalTime.of(19, 0);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public String getRegiao() { return regiao; }
    public void setRegiao(String regiao) { this.regiao = regiao; }
    public String getHorarioFuncionamento() { return horarioFuncionamento; }
    public void setHorarioFuncionamento(String horarioFuncionamento) { this.horarioFuncionamento = horarioFuncionamento; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getDiasFuncionamento() { return diasFuncionamento; }
    public void setDiasFuncionamento(String diasFuncionamento) { this.diasFuncionamento = diasFuncionamento; }
    public LocalTime getHoraAbertura() { return horaAbertura; }
    public void setHoraAbertura(LocalTime horaAbertura) { this.horaAbertura = horaAbertura; }
    public LocalTime getHoraFechamento() { return horaFechamento; }
    public void setHoraFechamento(LocalTime horaFechamento) { this.horaFechamento = horaFechamento; }

    public List<Vacina> getVacinasDisponiveis() {
        return vacinasDisponiveis;
    }

    public void setVacinasDisponiveis(List<Vacina> vacinasDisponiveis) {
        this.vacinasDisponiveis = vacinasDisponiveis;
    }

    public void adicionarVacinaDisponivel(Vacina vacina) {
        if (!this.vacinasDisponiveis.contains(vacina)) {
            this.vacinasDisponiveis.add(vacina);
        }
    }

    public boolean possuiVacina(Long vacinaId) {
        return this.vacinasDisponiveis.stream()
            .anyMatch(vacina -> vacina.getId() != null && vacina.getId().equals(vacinaId));
    }

    public boolean funcionaNoDia(String dia) {
        return diasFuncionamento != null && diasFuncionamento.contains(dia);
    }

    public boolean funcionaNoDia(DayOfWeek dia) {
        return dia != null && funcionaNoDia(dia.name());
    }

    public String getVacinaIdsCsv() {
        StringJoiner joiner = new StringJoiner(",");
        for (Vacina vacina : vacinasDisponiveis) {
            if (vacina.getId() != null) {
                joiner.add(vacina.getId().toString());
            }
        }
        return joiner.toString();
    }

    public String getDiasFuncionamentoLabel() {
        if (diasFuncionamento == null || diasFuncionamento.isBlank()) {
            return "Dias nao informados";
        }
        StringJoiner joiner = new StringJoiner(", ");
        for (String dia : diasFuncionamento.split(",")) {
            joiner.add(labelDia(dia));
        }
        return joiner.toString();
    }

    public String getHorarioFuncionamentoFormatado() {
        if (horaAbertura == null || horaFechamento == null) {
            return horarioFuncionamento;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return getDiasFuncionamentoLabel() + " - " + horaAbertura.format(formatter) + " as " + horaFechamento.format(formatter);
    }

    private String labelDia(String dia) {
        return switch (dia) {
            case "MONDAY" -> "Segunda";
            case "TUESDAY" -> "Terca";
            case "WEDNESDAY" -> "Quarta";
            case "THURSDAY" -> "Quinta";
            case "FRIDAY" -> "Sexta";
            case "SATURDAY" -> "Sabado";
            case "SUNDAY" -> "Domingo";
            default -> dia;
        };
    }
}
