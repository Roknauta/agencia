package com.roknauta.agencia.web.jasper.documento.orcamento;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.roknauta.agencia.dto.documento.OrcamentoDTO;
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
    private String tarifa;
    private BigDecimal valorAereo = BigDecimal.ZERO;
    private BigDecimal valorRodoviario = BigDecimal.ZERO;
    private BigDecimal valorHospedagem = BigDecimal.ZERO;
    private BigDecimal valorTransfer = BigDecimal.ZERO;
    private BigDecimal valorAlugueis = BigDecimal.ZERO;
    private BigDecimal valorSeguros = BigDecimal.ZERO;
    private BigDecimal valorESims = BigDecimal.ZERO;
    private BigDecimal taxas = BigDecimal.ZERO;
    private BigDecimal servicos = BigDecimal.ZERO;
    private BigDecimal desconto = BigDecimal.ZERO;
    private BigDecimal total = BigDecimal.ZERO;
    private BigDecimal valorPorPessoa = BigDecimal.ZERO;
    private String consultaPrecosEm;
    private String aviso = "Esta cotação é preliminar: não representa reserva nem garante preço ou disponibilidade. Se o cliente desejar prosseguir, a agência fará nova consulta e poderá solicitar uma pré-reserva. Os valores e o prazo da pré-reserva serão confirmados no contrato antes do aceite e do pagamento.";

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
        tarifa = orcamento.getTarifa();
        valorAereo = valor(orcamento.getValorAereo());
        valorRodoviario = totalRodoviario();
        valorHospedagem = soma(getHospedagens().stream().map(com.roknauta.agencia.web.jasper.documento.comum.HospedagemRelatorioDTO::getValorTotal));
        valorTransfer = soma(getTransfers().stream().map(com.roknauta.agencia.web.jasper.documento.comum.TransferRelatorioDTO::getValorTotal));
        valorAlugueis = soma(getAlugueis().stream().map(com.roknauta.agencia.web.jasper.documento.comum.AluguelVeiculoRelatorioDTO::getValorTotal));
        valorSeguros = soma(getSegurosViagem().stream().map(com.roknauta.agencia.web.jasper.documento.comum.SeguroViagemRelatorioDTO::getValorTotal));
        valorESims = soma(getESims().stream().map(com.roknauta.agencia.web.jasper.documento.comum.ESimRelatorioDTO::getValorTotal));
        taxas = valor(orcamento.getTaxas());
        servicos = valor(orcamento.getServicos());
        desconto = valor(orcamento.getDesconto());
        total = valorAereo.add(valorRodoviario).add(valorHospedagem).add(valorTransfer)
                .add(valorAlugueis).add(valorSeguros).add(valorESims).add(taxas).add(servicos).subtract(desconto);
        quantidadePassageiros = quantidade(orcamento.getAdultos())
                + quantidade(orcamento.getCriancas()) + quantidade(orcamento.getBebes());
        perfilPassageiros = Stream.of(
                        perfil(orcamento.getAdultos(), "adulto", "adultos"),
                        perfil(orcamento.getCriancas(), "criança", "crianças"),
                        perfil(orcamento.getBebes(), "bebê", "bebês"))
                .collect(Collectors.joining(" • "));
        valorPorPessoa = quantidadePassageiros == 0 ? BigDecimal.ZERO
                : total.divide(BigDecimal.valueOf(quantidadePassageiros), 2, RoundingMode.HALF_UP);
    }

    public java.util.List<com.roknauta.agencia.web.jasper.documento.comum.TrajetoRodoviarioRelatorioDTO> getPassagensRodoviarias() {
        return getTrajetosRodoviarios();
    }

    public String getRegras() {
        return getTrajetosAereos().stream()
                .flatMap(trajeto -> trajeto.getVoos().stream())
                .flatMap(voo -> Stream.of(voo.getRegraCancelamento(), voo.getRegraAlteracao(),
                        voo.getOutrasInformacoesTarifarias()))
                .filter(texto -> texto != null && !texto.isBlank())
                .distinct()
                .collect(Collectors.joining("\n"));
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
