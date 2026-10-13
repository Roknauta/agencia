package com.roknauta.agencia.web.jasper.documento.contrato;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import com.roknauta.agencia.dto.AgenciaDTO;
import com.roknauta.agencia.dto.documento.ContratoDTO;
import com.roknauta.agencia.web.jasper.JasperUtils;
import com.roknauta.agencia.web.jasper.documento.comum.BlocoTextoRelatorioDTO;
import com.roknauta.agencia.web.jasper.documento.comum.DocumentoViagemRelatorioDTO;
import com.roknauta.agencia.web.jasper.documento.comum.TrajetoAereoRelatorioDTO;
import com.roknauta.agencia.web.jasper.documento.comum.ViajanteRelatorioDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ContratoRelatorioDTO extends DocumentoViagemRelatorioDTO {

    private static final DateTimeFormatter DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private String numeroOrcamento;
    private String dataAssinatura;
    private String dataConsultaPrecos;
    private String referenciaPreReserva;
    private String preReservaValidaAte;
    private String razaoSocialAgencia;
    private String nomeFantasiaAgencia;
    private String cnpjAgencia;
    private String enderecoAgencia;
    private String emailAgencia;
    private String telefoneAgencia;
    private String representanteAgencia;
    private String cadasturAgencia;
    private String horarioAgencia;
    private String urgenciaAgencia;
    private List<BlocoTextoRelatorioDTO> clausulas = new ArrayList<>();
    private List<BlocoTextoRelatorioDTO> conteudoContrato = new ArrayList<>();

    public ContratoRelatorioDTO(ContratoDTO contrato, AgenciaDTO agencia) throws IOException {
        super(contrato);
        if (contrato == null) {
            return;
        }
        numeroOrcamento = contrato.getOrcamento() == null ? null : contrato.getOrcamento().getNumero();
        ZoneId fusoHorario = obterFusoHorario(agencia);
        dataAssinatura = formatarDataHora(contrato.getDataAssinatura(), fusoHorario);
        dataConsultaPrecos = formatarDataHora(contrato.getDataConsultaPrecos(), fusoHorario);
        referenciaPreReserva = contrato.getReferenciaPreReserva();
        preReservaValidaAte = formatarDataHora(contrato.getPreReservaValidaAte(), fusoHorario);
        if (agencia != null) {
            razaoSocialAgencia = agencia.getRazaoSocial();
            nomeFantasiaAgencia = agencia.getNomeFantasia();
            cnpjAgencia = agencia.getCnpj();
            enderecoAgencia = agencia.getEndereco();
            emailAgencia = agencia.getEmail();
            telefoneAgencia = agencia.getTelefone();
            representanteAgencia = agencia.getRepresentante();
            cadasturAgencia = agencia.getCadastur();
            horarioAgencia = agencia.getHorario();
            urgenciaAgencia = agencia.getUrgencia();
        }
        clausulas = carregarClausulas();
        conteudoContrato = montarConteudoContrato();
    }

    private ZoneId obterFusoHorario(AgenciaDTO agencia) {
        String fuso = agencia == null || agencia.getCidade() == null ? null : agencia.getCidade().getFusoHorario();
        if (fuso == null || fuso.isBlank()) {
            throw new IllegalArgumentException(
                "Configure uma cidade com fuso horário na agência para gerar o contrato.");
        }
        return ZoneId.of(fuso);
    }

    private String formatarDataHora(Instant instante, ZoneId fusoHorario) {
        return instante == null ? null : DATA_HORA.withZone(fusoHorario).format(instante);
    }

    private List<BlocoTextoRelatorioDTO> montarConteudoContrato() {
        List<BlocoTextoRelatorioDTO> blocos = new ArrayList<>();
        adicionar(blocos, "Dados da agência",
            juntar(linha("Razão social", razaoSocialAgencia), linha("CNPJ", cnpjAgencia),
                linha("Endereço", enderecoAgencia), linha("Representante", representanteAgencia),
                linha("CADASTUR", cadasturAgencia), linha("E-mail", emailAgencia), linha("Telefone", telefoneAgencia),
                linha("Horário de atendimento", horarioAgencia), linha("Urgência", urgenciaAgencia)));
        adicionar(blocos, "Viajantes",
            getViajantes().stream().map(viajante -> juntar(linha("Nome", viajante.getNome()),
                linha("Tipo de documento", viajante.getTipoDocumento()), linha("Documento", viajante.getDocumento()),
                linha("Data de nascimento", viajante.getDataNascimento()))).filter(texto -> !texto.isBlank())
                .collect(java.util.stream.Collectors.joining("\n\n")));
        adicionar(blocos, "Consulta de preços e pré-reserva",
            juntar(linha("Preços consultados em", dataConsultaPrecos),
                linha("Referência da pré-reserva", referenciaPreReserva),
                linha("Pré-reserva válida até", preReservaValidaAte)));
        adicionar(blocos, "Passagens aéreas", reservasAereas());
        getTrajetosRodoviarios().forEach(trajeto -> adicionar(blocos, "Passagem rodoviária",
            juntar(linha("Transportadora", trajeto.getTransportadora()),
                linha("Trecho", trajeto.getOrigem() + " → " + trajeto.getDestino()),
                linha("Embarque", trajeto.getSaida()), linha("Chegada", trajeto.getChegada()),
                linha("Categoria", trajeto.getCategoria()), linha("Localizador", trajeto.getLocalizador()),
                linha("Terminais",
                    formatarLista(" → ", trajeto.getTerminalEmbarque(), trajeto.getTerminalDesembarque())),
                linha("Bagagem", trajeto.getBagagem()), linha("Valor", trajeto.getValorTotal()))));
        getHospedagens().forEach(hospedagem -> adicionar(blocos, "Hospedagem",
            juntar(linha("Hotel", hospedagem.getNome()), linha("Cidade", hospedagem.getCidade()),
                linha("Endereço", hospedagem.getEndereco()), linha("Check-in", hospedagem.getCheckIn()),
                linha("Check-out", hospedagem.getCheckOut()), linha("Acomodação", hospedagem.getAcomodacao()),
                linha("Quartos", hospedagem.getQuartos()), linha("Regime", hospedagem.getRegime()),
                linha("Incluídos", hospedagem.getIncluidos()), linha("Não incluídos", hospedagem.getNaoIncluidos()),
                linha("Cancelamento", hospedagem.getCancelamento()), linha("Localizador", hospedagem.getLocalizador()),
                linha("Valor", hospedagem.getValorTotal()))));
        getTransfers().forEach(transfer -> adicionar(blocos, "Transfer",
            juntar(linha("Prestador", transfer.getPrestador()), linha("Modalidade", transfer.getTipo()),
                linha("Percurso", transfer.getResumoPercursos()), linha("Data e hora", transfer.getResumoDatas()),
                linha("Passageiros", transfer.getPassageiros()), linha("Localizador", transfer.getLocalizador()),
                linha("Observações", transfer.getObservacoes()), linha("Valor", transfer.getValorTotal()))));
        getAlugueis().forEach(aluguel -> adicionar(blocos, "Aluguel de veículo",
            juntar(linha("Locadora", aluguel.getLocadora()), linha("Categoria", aluguel.getCategoria()),
                linha("Condutores", aluguel.getCondutor()), linha("Retirada", aluguel.getResumoRetirada()),
                linha("Local de retirada", aluguel.getLocalRetirada()),
                linha("Devolução", aluguel.getResumoDevolucao()),
                linha("Local de devolução", aluguel.getLocalDevolucao()),
                linha("Localizador", aluguel.getLocalizador()), linha("Coberturas", aluguel.getCoberturas()),
                linha("Condições", aluguel.getCondicoes()), linha("Valor", aluguel.getValorTotal()))));
        getSegurosViagem().forEach(seguro -> adicionar(blocos, "Seguro viagem",
            juntar(linha("Seguradora", seguro.getSeguradora()), linha("Plano", seguro.getPlano()),
                linha("Vigência", formatarLista(" a ", seguro.getInicioVigencia(), seguro.getFimVigencia())),
                linha("Viajantes cobertos", seguro.getViajantesCobertos()),
                linha("Resumo da cobertura", seguro.getResumoCobertura()), linha("Apólice", seguro.getNumeroApolice()),
                linha("Valor", seguro.getValorTotal()))));
        getESims().forEach(esim -> adicionar(blocos, "eSIM",
            juntar(linha("Operadora", esim.getOperadora()), linha("Plano", esim.getPlano()),
                linha("Franquia de dados", esim.getFranquiaDados()), linha("Cobertura", esim.getCobertura()),
                linha("Duração", esim.getDuracaoDias() == null ? null : esim.getDuracaoDias() + " dias"),
                linha("Quantidade", esim.getQuantidade()), linha("Valor", esim.getValorTotal()))));
        BigDecimal total = totalServicos();
        if (total.signum() > 0) {
            adicionar(blocos, "Valor total dos serviços", linha("Total", total));
        }
        blocos.addAll(clausulas);
        return blocos;
    }

    private String reservasAereas() {
        StringJoiner texto = new StringJoiner("\n\n");
        for (TrajetoAereoRelatorioDTO trajeto : getTrajetosAereos()) {
            for (var voo : trajeto.getVoos()) {
                StringJoiner trechoTexto = new StringJoiner("\n");
                adicionarLinha(trechoTexto, "Sentido", trajeto.getSentido());
                adicionarLinha(trechoTexto, "Companhia",
                    voo.getCompanhia() == null ? null : voo.getCompanhia().getNome());
                adicionarLinha(trechoTexto, "Localizador", voo.getLocalizador());
                adicionarLinha(trechoTexto, "Cabine", voo.getCabine());
                adicionarLinha(trechoTexto, "Tarifa", voo.getTarifa());
                adicionarLinha(trechoTexto, "Voo",
                    formatarLista(" ", voo.getNumero(), formatarLista(" → ", voo.getOrigem(), voo.getDestino()),
                        dataHora(voo.getSaida()), dataHora(voo.getChegada())));
                if (voo.getBagagens() != null) {
                    voo.getBagagens()
                        .forEach(bagagem -> adicionarLinha(trechoTexto, "Bagagem", bagagem.getDescricaoRelatorio()));
                }
                texto.add(trechoTexto.toString());
            }
            adicionarLinha(texto, "Valor total " + trajeto.getSentido(), trajeto.getValorTotal());
        }
        return texto.toString();
    }

    private BigDecimal totalServicos() {
        BigDecimal total = BigDecimal.ZERO;
        total = somar(total, getTrajetosAereos().stream().map(item -> item.getValorTotal()).toList());
        total = somar(total, getTrajetosRodoviarios().stream().map(item -> item.getValorTotal()).toList());
        total = somar(total, getHospedagens().stream().map(item -> item.getValorTotal()).toList());
        total = somar(total, getTransfers().stream().map(item -> item.getValorTotal()).toList());
        total = somar(total, getAlugueis().stream().map(item -> item.getValorTotal()).toList());
        total = somar(total, getSegurosViagem().stream().map(item -> item.getValorTotal()).toList());
        total = somar(total, getESims().stream().map(item -> item.getValorTotal()).toList());
        return total;
    }

    private static BigDecimal somar(BigDecimal total, List<BigDecimal> valores) {
        return total.add(valores.stream().filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    private static String dataHora(java.time.LocalDateTime dataHora) {
        return dataHora == null ? null : dataHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    private static String juntar(Object... valores) {
        return java.util.Arrays.stream(valores).filter(Objects::nonNull).map(Object::toString)
            .filter(valor -> !valor.isBlank()).collect(java.util.stream.Collectors.joining("\n"));
    }

    private static String formatarLista(String separador, Object... valores) {
        return java.util.Arrays.stream(valores).filter(Objects::nonNull).map(Object::toString)
            .filter(valor -> !valor.isBlank()).collect(java.util.stream.Collectors.joining(separador));
    }

    private static String linha(String rotulo, Object valor) {
        if (valor == null || valor.toString().isBlank()) {
            return "";
        }
        String texto =
            valor instanceof LocalDate data ? data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                : valor instanceof LocalDateTime dataHora
                    ? dataHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                    : valor instanceof BigDecimal dinheiro ? java.text.NumberFormat
                        .getCurrencyInstance(java.util.Locale.forLanguageTag("pt-BR")).format(dinheiro)
                        : valor.toString();
        if (valor instanceof String) {
            try {
                texto = LocalDateTime.parse(texto).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            } catch (java.time.format.DateTimeParseException ignored) {
                // O texto não representa uma data ISO; mantém o valor original.
            }
        }
        return rotulo + ": " + texto;
    }

    private static void adicionarLinha(StringJoiner texto, String rotulo, Object valor) {
        String linha = linha(rotulo, valor);
        if (!linha.isBlank()) {
            texto.add(linha);
        }
    }

    private static void adicionar(List<BlocoTextoRelatorioDTO> blocos, String titulo, String texto) {
        if (texto != null && !texto.isBlank()) {
            blocos.add(new BlocoTextoRelatorioDTO(titulo, texto, false));
        }
    }

    private List<BlocoTextoRelatorioDTO> carregarClausulas() throws IOException {
        try (BufferedReader leitor = new BufferedReader(
            new InputStreamReader(JasperUtils.abrirRecurso("contrato-clausulas.txt"), StandardCharsets.UTF_8))) {
            return leitor.lines().filter(linha -> !linha.isBlank()).map(this::parsearClausula).filter(Objects::nonNull)
                .toList();
        }
    }

    private BlocoTextoRelatorioDTO parsearClausula(String linha) {
        String[] partes = linha.split("\\|", 3);
        if (partes.length < 3) {
            return null;
        }
        String titulo = partes[0].trim();
        boolean destaque = Boolean.parseBoolean(partes[1].trim());
        String texto = partes[2].trim();
        return new BlocoTextoRelatorioDTO(titulo, texto, destaque);
    }
}
