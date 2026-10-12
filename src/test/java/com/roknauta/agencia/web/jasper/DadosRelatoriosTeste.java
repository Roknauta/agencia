package com.roknauta.agencia.web.jasper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Base64;
import java.util.List;

import com.roknauta.agencia.domain.CabineAerea;
import com.roknauta.agencia.domain.SentidoVoo;
import com.roknauta.agencia.domain.TipoBagagem;
import com.roknauta.agencia.domain.documento.CategoriaTrajetoRodoviario;
import com.roknauta.agencia.domain.documento.CategoriaVeiculo;
import com.roknauta.agencia.domain.documento.LocadoraVeiculo;
import com.roknauta.agencia.domain.documento.RegimeHospedagem;
import com.roknauta.agencia.domain.documento.TipoDocumentoViajante;
import com.roknauta.agencia.domain.documento.TipoTransfer;
import com.roknauta.agencia.dto.AeroportoDTO;
import com.roknauta.agencia.dto.BagagemCompanhiaAereaDTO;
import com.roknauta.agencia.dto.CompanhiaAereaDTO;
import com.roknauta.agencia.dto.HotelDTO;
import com.roknauta.agencia.dto.CidadeDTO;
import com.roknauta.agencia.dto.PaisDTO;
import com.roknauta.agencia.dto.TarifaAereaDTO;
import com.roknauta.agencia.dto.documento.AluguelVeiculoDTO;
import com.roknauta.agencia.dto.documento.BagagemReservaDTO;
import com.roknauta.agencia.dto.documento.ESimDTO;
import com.roknauta.agencia.dto.documento.FotoHospedagemDTO;
import com.roknauta.agencia.dto.documento.HospedagemDTO;
import com.roknauta.agencia.dto.documento.OrcamentoDTO;
import com.roknauta.agencia.dto.documento.ReservaDTO;
import com.roknauta.agencia.dto.documento.SeguroViagemDTO;
import com.roknauta.agencia.dto.documento.TrajetoAereoDTO;
import com.roknauta.agencia.dto.documento.TrajetoRodoviarioDTO;
import com.roknauta.agencia.dto.documento.TrechoReservaDTO;
import com.roknauta.agencia.dto.documento.TransferDTO;
import com.roknauta.agencia.dto.documento.ViajanteDTO;
import com.roknauta.agencia.dto.documento.VooDTO;

public final class DadosRelatoriosTeste {

    private static final LocalDate DATA_PARTIDA = LocalDate.of(2027, 2, 10);

    private DadosRelatoriosTeste() {
    }

    public static OrcamentoDTO orcamentoCompleto() {
        OrcamentoDTO orcamento = new OrcamentoDTO();
        preencherDocumento(orcamento);
        orcamento.setValidadeAte(LocalDate.of(2026, 10, 20));
        orcamento.setAdultos(2);
        orcamento.setCriancas(1);
        orcamento.setBebes(1);
        orcamento.setPagamento("Pix ou cartão em até 6 vezes");
        orcamento.setTarifa("Econômica promocional");
        orcamento.setValorAereo(new BigDecimal("4500.00"));
        orcamento.setTaxas(new BigDecimal("250.00"));
        orcamento.setServicos(new BigDecimal("100.00"));
        orcamento.setDesconto(new BigDecimal("50.00"));
        return orcamento;
    }

    public static void preencherDocumento(com.roknauta.agencia.dto.documento.DocumentoViagemDTO documento) {
        documento.setNumero("DOC-TESTE-2027-001");
        documento.setCliente("Família Exemplo");
        documento.setConsultor("Ana Consultora");
        documento.setDataDocumento(LocalDate.of(2026, 10, 9));
        documento.setOrigem(cidade("Belo Horizonte", "MG"));
        documento.setDestino("Lisboa, Portugal");
        documento.setInicio(DATA_PARTIDA);
        documento.setFim(DATA_PARTIDA.plusDays(8));
        documento.setObservacoes("Apresentar-se com antecedência e conferir os documentos.");

        ViajanteDTO adulto = viajante("Joana Exemplo", "12345678900", LocalDate.of(1988, 3, 14));
        ViajanteDTO crianca = viajante("Lucas Exemplo", "98765432100", LocalDate.of(2017, 8, 2));
        documento.setViajantes(List.of(adulto, crianca));
        documento.setTrajetosAereos(List.of(trajetoIda(), trajetoVolta()));
        documento.setTrajetosRodoviarios(List.of(trajetoRodoviario()));
        documento.setHospedagens(List.of(hospedagem()));
        documento.setTransfers(List.of(transfer("Aeroporto → hotel", new BigDecimal("180.00")),
                transfer("Hotel → aeroporto", new BigDecimal("170.00"))));
        documento.setAlugueis(List.of(aluguel("LOC-101", new BigDecimal("900.00")),
                aluguel("LOC-102", new BigDecimal("150.00"))));
        documento.setSegurosViagem(List.of(seguro(adulto, crianca)));
        documento.setESims(List.of(esim()));
    }

    public static TrajetoAereoDTO trajetoIda() {
        ReservaDTO reserva = new ReservaDTO();
        reserva.setLocalizador("ABC123");
        reserva.setTrechos(List.of(trecho("JJ1234", "CNF", "GRU", LocalTime.of(8, 0), LocalTime.of(9, 15), DATA_PARTIDA),
                trecho("JJ5678", "GRU", "LIS", LocalTime.of(12, 0), LocalTime.of(23, 5), DATA_PARTIDA)));
        reserva.setBagagens(List.of(bagagem("Despachada", 1), bagagem("Bagagem de mão", 1)));
        TrajetoAereoDTO trajeto = new TrajetoAereoDTO();
        trajeto.setSentido(SentidoVoo.IDA);
        trajeto.setReservas(List.of(reserva));
        trajeto.setValorTotal(new BigDecimal("2600.00"));
        return trajeto;
    }

    public static TrajetoAereoDTO trajetoVolta() {
        ReservaDTO reserva = new ReservaDTO();
        reserva.setLocalizador("XYZ789");
        reserva.setTrechos(List.of(trecho("TP1020", "LIS", "CNF", LocalTime.of(10, 0), LocalTime.of(18, 20), DATA_PARTIDA.plusDays(8))));
        reserva.setBagagens(List.of(bagagem("Despachada", 1)));
        TrajetoAereoDTO trajeto = new TrajetoAereoDTO();
        trajeto.setSentido(SentidoVoo.VOLTA);
        trajeto.setReservas(List.of(reserva));
        trajeto.setValorTotal(new BigDecimal("1900.00"));
        return trajeto;
    }

    public static TrechoReservaDTO trecho(String numero, String origem, String destino,
                                    LocalTime partida, LocalTime chegada, LocalDate data) {
        CompanhiaAereaDTO companhia = new CompanhiaAereaDTO();
        companhia.setNome("Companhia Exemplo");
        VooDTO voo = new VooDTO();
        voo.setNumero(numero);
        voo.setDataReferencia(data);
        voo.setCompanhiaAerea(companhia);
        voo.setOrigem(aeroporto(origem));
        voo.setDestino(aeroporto(destino));
        voo.setPartida(partida);
        voo.setChegada(chegada);
        TrechoReservaDTO trecho = new TrechoReservaDTO();
        trecho.setVoo(voo);
        trecho.setDataPartida(data);
        trecho.setDataChegada(chegada.isBefore(partida) ? data.plusDays(1) : data);
        trecho.setCabine(CabineAerea.ECONOMICA);
        TarifaAereaDTO tarifa = new TarifaAereaDTO();
        tarifa.setNome("Light Flex");
        tarifa.setDataReferencia(data);
        tarifa.setRegraCancelamento("Cancelamento sujeito a multa do fornecedor.");
        tarifa.setRegraAlteracao("Alteração sujeita à diferença tarifária.");
        tarifa.setOutrasInformacoes("Bagagem conforme franquia contratada.");
        trecho.setTarifaAerea(tarifa);
        return trecho;
    }

    private static AeroportoDTO aeroporto(String iata) {
        AeroportoDTO aeroporto = new AeroportoDTO();
        aeroporto.setCodigoIata(iata);
        aeroporto.setNome("Aeroporto " + iata);
        aeroporto.setCidadeAtendida(cidade(iata, "Brasil"));
        return aeroporto;
    }

    private static BagagemReservaDTO bagagem(String descricao, int quantidade) {
        BagagemReservaDTO bagagem = new BagagemReservaDTO();
        bagagem.setDescricao(descricao);
        bagagem.setQuantidade(quantidade);
        BagagemCompanhiaAereaDTO referencia = new BagagemCompanhiaAereaDTO();
        referencia.setTipoBagagem(TipoBagagem.BAGAGEM_DESPACHADA);
        referencia.setDataReferencia(LocalDate.of(2026, 10, 1));
        referencia.setAlturaCm(55.0);
        referencia.setLarguraCm(35.0);
        referencia.setProfundidadeCm(25.0);
        referencia.setPesoMaximo(23.0);
        bagagem.setBagagemCompanhiaAerea(referencia);
        return bagagem;
    }

    private static TrajetoRodoviarioDTO trajetoRodoviario() {
        TrajetoRodoviarioDTO trajeto = new TrajetoRodoviarioDTO();
        trajeto.setTransportadora("Viação Exemplo");
        trajeto.setOrigem(cidade("Lisboa", "Lisboa"));
        trajeto.setDestino(cidade("Porto", "Porto"));
        trajeto.setTerminalEmbarque("Terminal Oriente");
        trajeto.setTerminalDesembarque("Terminal Campanhã");
        trajeto.setCategoria(CategoriaTrajetoRodoviario.EXECUTIVO);
        trajeto.setLocalizador("BUS-456");
        trajeto.setBagagem("1 mala de até 23 kg");
        trajeto.setSaida(DATA_PARTIDA.plusDays(2).atTime(9, 30));
        trajeto.setChegada(DATA_PARTIDA.plusDays(2).atTime(13, 0));
        trajeto.setValorTotal(new BigDecimal("75.00"));
        return trajeto;
    }

    private static HospedagemDTO hospedagem() {
        HotelDTO hotel = new HotelDTO();
        hotel.setNome("Hotel Central Lisboa");
        hotel.setCidade(cidade("Lisboa", "Lisboa"));
        hotel.setEndereco("Rua de Exemplo, 100");
        HospedagemDTO hospedagem = new HospedagemDTO();
        hospedagem.setHotelSelecionado(hotel);
        hospedagem.setCheckIn(DATA_PARTIDA.atTime(15, 0));
        hospedagem.setCheckOut(DATA_PARTIDA.plusDays(8).atTime(11, 0));
        hospedagem.setLocalizador("HTL-789");
        hospedagem.setAcomodacao("Quarto familiar");
        hospedagem.setQuartos(1);
        hospedagem.setRegime(RegimeHospedagem.CAFE_DA_MANHA);
        hospedagem.setCancelamento("Cancelamento grátis até 48 horas antes.");
        hospedagem.setIncluidos("Café da manhã e Wi-Fi");
        hospedagem.setNaoIncluidos("Estacionamento");
        hospedagem.setValorTotal(new BigDecimal("1800.00"));
        hospedagem.setObservacoes("Quarto para dois adultos e duas crianças.");
        FotoHospedagemDTO foto = new FotoHospedagemDTO();
        foto.setConteudo(Base64.getDecoder().decode("iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mP8/x8AAwMCAO+/l1sAAAAASUVORK5CYII="));
        hospedagem.setFotos(List.of(foto));
        return hospedagem;
    }

    private static TransferDTO transfer(String percurso, BigDecimal valor) {
        TransferDTO transfer = new TransferDTO();
        transfer.setPrestador("Transfer Exemplo");
        transfer.setTipo(TipoTransfer.PRIVATIVO);
        transfer.setLocalizador("TR-" + valor.intValue());
        transfer.setOrigem(percurso.startsWith("Aeroporto") ? "Aeroporto de Lisboa" : "Hotel Central Lisboa");
        transfer.setDestino(percurso.startsWith("Aeroporto") ? "Hotel Central Lisboa" : "Aeroporto de Lisboa");
        transfer.setDataHora(DATA_PARTIDA.atTime(12, 0));
        transfer.setFusoHorario("Europe/Lisbon");
        transfer.setPassageiros(4);
        transfer.setObservacoes(percurso);
        transfer.setValorTotal(valor);
        return transfer;
    }

    private static AluguelVeiculoDTO aluguel(String localizador, BigDecimal valor) {
        AluguelVeiculoDTO aluguel = new AluguelVeiculoDTO();
        aluguel.setLocadora(LocadoraVeiculo.LOCALIZA);
        aluguel.setCategoria(CategoriaVeiculo.COMPACTO);
        aluguel.setCondutores(List.of("Joana Exemplo", "Marcos Exemplo"));
        aluguel.setLocalizador(localizador);
        aluguel.setLocalRetirada("Aeroporto de Lisboa");
        aluguel.setLocalDevolucao("Aeroporto do Porto");
        aluguel.setCoberturas("Proteção básica e contra terceiros");
        aluguel.setCondicoes("Caução no cartão, quilometragem livre");
        aluguel.setObservacoes("Levar carteira de motorista válida.");
        aluguel.setRetirada(DATA_PARTIDA.plusDays(2).atTime(14, 0));
        aluguel.setDevolucao(DATA_PARTIDA.plusDays(5).atTime(10, 0));
        aluguel.setFusoHorarioRetirada("Europe/Lisbon");
        aluguel.setFusoHorarioDevolucao("Europe/Lisbon");
        aluguel.setValorTotal(valor);
        return aluguel;
    }

    private static SeguroViagemDTO seguro(ViajanteDTO... viajantes) {
        SeguroViagemDTO seguro = new SeguroViagemDTO();
        seguro.setSeguradora("Seguradora Exemplo");
        seguro.setPlano("Europa Essencial");
        seguro.setResumoCobertura("Cobertura médica, bagagem e assistência");
        seguro.setNumeroApolice("POL-001");
        seguro.setInicioVigencia(DATA_PARTIDA);
        seguro.setFimVigencia(DATA_PARTIDA.plusDays(8));
        seguro.setValorTotal(new BigDecimal("120.00"));
        seguro.setViajantesCobertos(List.of(viajantes));
        return seguro;
    }

    private static ESimDTO esim() {
        ESimDTO esim = new ESimDTO();
        esim.setOperadora("Conecta Europa");
        esim.setPlano("10 GB Europa");
        esim.setFranquiaDados("10 GB");
        esim.setCobertura("Portugal, Espanha e França");
        esim.setDuracaoDias(10);
        esim.setInstrucoesInstalacao("Instalar antes do embarque e ativar ao chegar.");
        esim.setCodigosAtivacao(List.of("LPA:1$SM-DP+$TESTE-001", "LPA:1$SM-DP+$TESTE-002"));
        esim.setQuantidade(2);
        esim.setValorTotal(new BigDecimal("60.00"));
        return esim;
    }

    private static ViajanteDTO viajante(String nome, String documento, LocalDate nascimento) {
        ViajanteDTO viajante = new ViajanteDTO();
        viajante.setNome(nome);
        viajante.setTipoDocumento(TipoDocumentoViajante.CPF);
        viajante.setDocumento(documento);
        viajante.setDataNascimento(nascimento);
        return viajante;
    }

    private static CidadeDTO cidade(String nome, String estado) {
        PaisDTO pais = new PaisDTO();
        pais.setNome("Brasil");
        pais.setSigla("BR");
        CidadeDTO dto = new CidadeDTO();
        dto.setNome(nome);
        dto.setEstado(estado);
        dto.setPais(pais);
        return dto;
    }
}
