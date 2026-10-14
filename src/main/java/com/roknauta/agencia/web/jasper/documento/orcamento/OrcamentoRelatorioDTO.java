package com.roknauta.agencia.web.jasper.documento.orcamento;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.List;

import com.roknauta.agencia.dto.documento.OrcamentoDTO;
import com.roknauta.agencia.domain.documento.Moeda;
import com.roknauta.agencia.dto.documento.ViajanteDTO;
import com.roknauta.agencia.web.jasper.documento.comum.TrajetoAereoRelatorioDTO;
import com.roknauta.agencia.web.jasper.documento.comum.TrajetoRodoviarioRelatorioDTO;
import com.roknauta.agencia.web.jasper.documento.comum.HospedagemRelatorioDTO;
import com.roknauta.agencia.web.jasper.documento.comum.TransferRelatorioDTO;
import com.roknauta.agencia.web.jasper.documento.comum.AluguelVeiculoRelatorioDTO;
import com.roknauta.agencia.web.jasper.documento.comum.SeguroViagemRelatorioDTO;
import com.roknauta.agencia.web.jasper.documento.comum.ESimRelatorioDTO;
import com.roknauta.agencia.web.jasper.documento.comum.DocumentoViagemRelatorioDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrcamentoRelatorioDTO extends DocumentoViagemRelatorioDTO {

    private static final DateTimeFormatter DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private String elaboradoEm;
    private String validade;
    private String perfilPassageiros;
    private int quantidadePassageiros;
    private String pagamento;
    private Moeda moeda;
    private BigDecimal valorAereo = BigDecimal.ZERO;
    private BigDecimal valorRodoviario = BigDecimal.ZERO;
    private BigDecimal valorHospedagem = BigDecimal.ZERO;
    private BigDecimal valorTransfer = BigDecimal.ZERO;
    private BigDecimal valorAlugueis = BigDecimal.ZERO;
    private BigDecimal valorSeguros = BigDecimal.ZERO;
    private BigDecimal valorESims = BigDecimal.ZERO;
    private BigDecimal servicos = BigDecimal.ZERO;
    private BigDecimal desconto = BigDecimal.ZERO;
    private BigDecimal total = BigDecimal.ZERO;
    private BigDecimal valorPorPessoa = BigDecimal.ZERO;
    private boolean possuiPassagensAereas;
    private boolean possuiPassagensRodoviarias;
    private String consultaPrecosEm;
    private String aviso =
        "Esta cotação é preliminar: não representa reserva nem garante preço ou disponibilidade. Se o cliente desejar prosseguir, a agência fará nova consulta e poderá solicitar uma pré-reserva. Os valores e o prazo da pré-reserva serão confirmados no contrato antes do aceite e do pagamento.";

    public OrcamentoRelatorioDTO(OrcamentoDTO orcamento) {
        super(orcamento);
        if (orcamento == null) {
            return;
        }
        LocalDateTime geradoEm = LocalDateTime.now();
        elaboradoEm = geradoEm.format(DATA_HORA);
        consultaPrecosEm = elaboradoEm;
        validade = orcamento.getValidadeAte() == null ? null : orcamento.getValidadeAte().format(DATA);
        pagamento = orcamento.getPagamento();
        moeda = orcamento.getMoeda();
        possuiPassagensAereas = !getTrajetosAereos().isEmpty();
        possuiPassagensRodoviarias = !getTrajetosRodoviarios().isEmpty();
        valorAereo = soma(getTrajetosAereos().stream().map(TrajetoAereoRelatorioDTO::getValorTotal));
        valorRodoviario = totalRodoviario();
        valorHospedagem = soma(getHospedagens().stream().map(HospedagemRelatorioDTO::getValorTotal));
        valorTransfer = soma(getTransfers().stream().map(TransferRelatorioDTO::getValorTotal));
        valorAlugueis = soma(getAlugueis().stream().map(AluguelVeiculoRelatorioDTO::getValorTotal));
        valorSeguros = soma(getSegurosViagem().stream().map(SeguroViagemRelatorioDTO::getValorTotal));
        valorESims = soma(getESims().stream().map(ESimRelatorioDTO::getValorTotal));
        servicos = valor(orcamento.getServicos());
        desconto = valor(orcamento.getDesconto());
        total = valorAereo.add(valorRodoviario).add(valorHospedagem).add(valorTransfer).add(valorAlugueis)
            .add(valorSeguros).add(valorESims).add(servicos).subtract(desconto);
        quantidadePassageiros =
            quantidade(orcamento.getAdultos()) + quantidade(orcamento.getCriancas()) + quantidade(orcamento.getBebes());
        perfilPassageiros = Stream
            .of(perfil(orcamento.getAdultos(), "adulto", "adultos"),
                perfil(orcamento.getCriancas(), "criança", "crianças"), perfil(orcamento.getBebes(), "bebê", "bebês"))
            .collect(Collectors.joining(" • "));
        valorPorPessoa = quantidadePassageiros == 0 ? BigDecimal.ZERO
            : total.divide(BigDecimal.valueOf(quantidadePassageiros), 2, RoundingMode.HALF_UP);
    }

    public List<TrajetoRodoviarioRelatorioDTO> getPassagensRodoviarias() {
        return getTrajetosRodoviarios();
    }

    public String getRegras() {
        return getTrajetosAereos().stream().flatMap(trajeto -> trajeto.getVoos().stream())
            .flatMap(voo -> Stream.of(voo.getRegraCancelamento(), voo.getRegraAlteracao(),
                voo.getOutrasInformacoesTarifarias()))
            .filter(texto -> texto != null && !texto.isBlank()).distinct().collect(Collectors.joining("\n"));
    }

    public boolean isPossuiSeguros() {
        return !getSegurosViagem().isEmpty();
    }

    public boolean isPossuiESims() {
        return !getESims().isEmpty();
    }

    public boolean isPossuiHospedagens() {
        return !getHospedagens().isEmpty();
    }

    public boolean isPossuiTransfers() {
        return !getTransfers().isEmpty();
    }

    public boolean isPossuiAlugueis() {
        return !getAlugueis().isEmpty();
    }

    public String getResumoSeguros() {
        return getSegurosViagem().stream()
            .map(seguro -> Stream
                .of("Seguradora: " + Objects.toString(seguro.getSeguradora(), ""),
                    "Plano: " + Objects.toString(seguro.getPlano(), ""),
                    "Apólice: " + Objects.toString(seguro.getNumeroApolice(), ""),
                    "Vigência: " + Objects.toString(seguro.getInicioVigencia(), "") + " a "
                        + Objects.toString(seguro.getFimVigencia(), ""),
                    "Viajantes: " + Objects.toString(seguro.getViajantesCobertos(), ""),
                    "Cobertura: " + Objects.toString(seguro.getResumoCobertura(), ""), seguro.getResumoFinanceiro(),
                    "Observações: " + Objects.toString(seguro.getObservacoes(), ""))
                .filter(linha -> !linha.endsWith(": ")).collect(Collectors.joining("\n")))
            .collect(Collectors.joining("\n\n"));
    }

    public String getResumoESims() {
        return getESims().stream().map(esim -> Stream.of("Operadora: " + Objects.toString(esim.getOperadora(), ""),
            "Plano: " + Objects.toString(esim.getPlano(), ""),
            "Franquia: " + Objects.toString(esim.getFranquiaDados(), ""),
            "Cobertura: " + Objects.toString(esim.getCobertura(), ""),
            "Duração: " + Objects.toString(esim.getDuracaoDias(), "") + " dias", "Quantidade: " + esim.getQuantidade(),
            "Códigos de ativação: " + Objects.toString(esim.getCodigosAtivacao(), ""),
            "Instruções: " + Objects.toString(esim.getInstrucoesInstalacao(), ""), esim.getResumoFinanceiro(),
            "Observações: " + Objects.toString(esim.getObservacoes(), "")).filter(linha -> !linha.endsWith(": "))
            .collect(Collectors.joining("\n"))).collect(Collectors.joining("\n\n"));
    }

    public String getResumoValoresAereos() {
        return getTrajetosAereos().stream().map(trajeto -> Objects.toString(trajeto.getSentido(), "Trecho") + ": "
            + formatarDinheiro(trajeto.getValorTotal())).collect(Collectors.joining(" • "));
    }

    public String getResumoValoresRodoviarios() {
        return getTrajetosRodoviarios().stream()
            .map(trajeto -> Objects.toString(trajeto.getTransportadora(), "Passagem rodoviária") + " ("
                + Objects.toString(trajeto.getOrigem(), "") + " → " + Objects.toString(trajeto.getDestino(), "") + "): "
                + formatarDinheiro(trajeto.getValorTotal()))
            .collect(Collectors.joining(" • "));
    }

    private String formatarDinheiro(BigDecimal valor) {
        if (valor == null) {
            return "";
        }
        String simbolo = moeda == null ? "R$" : moeda.getSimbolo();
        return simbolo + " "
            + java.text.NumberFormat.getNumberInstance(java.util.Locale.forLanguageTag("pt-BR")).format(valor);
    }

    private BigDecimal totalRodoviario() {
        return soma(getTrajetosRodoviarios().stream()
            .map(com.roknauta.agencia.web.jasper.documento.comum.TrajetoRodoviarioRelatorioDTO::getValorTotal));
    }

    private static BigDecimal soma(java.util.stream.Stream<BigDecimal> valores) {
        return valores.filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static BigDecimal valor(BigDecimal valor) {
        return valor == null ? BigDecimal.ZERO : valor;
    }

    private static int quantidade(Integer valor) {
        return valor == null ? 0 : valor;
    }

    private static String perfil(Integer valor, String singular, String plural) {
        int quantidade = quantidade(valor);
        return quantidade + " " + (quantidade == 1 ? singular : plural);
    }
}
