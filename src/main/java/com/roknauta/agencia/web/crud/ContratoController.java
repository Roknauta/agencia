package com.roknauta.agencia.web.crud;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.roknauta.agencia.domain.documento.Moeda;
import com.roknauta.agencia.domain.documento.StatusContrato;
import com.roknauta.agencia.domain.documento.StatusOrcamento;
import com.roknauta.agencia.domain.documento.TipoDocumentoViajante;
import com.roknauta.agencia.dto.documento.ContratoDTO;
import com.roknauta.agencia.dto.documento.OrcamentoDTO;
import com.roknauta.agencia.dto.documento.ViajanteDTO;
import com.roknauta.agencia.service.AgenciaService;
import com.roknauta.agencia.service.documento.ContratoService;
import com.roknauta.agencia.service.documento.OrcamentoService;
import com.roknauta.agencia.web.Msg;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import net.sf.jasperreports.engine.JRException;
import org.springframework.stereotype.Component;

@Component
@ViewScoped
public class ContratoController extends CrudControllerBase<ContratoDTO, ContratoService> {

    private final OrcamentoService orcamentoService;
    private final AgenciaService agenciaService;
    private OrcamentoDTO orcamentoSelecionado;
    private LocalDateTime dataConsultaPrecos;
    private LocalDateTime preReservaValidaAte;
    private ViajanteDTO viajanteForm = new ViajanteDTO();

    public ContratoController(ContratoService service, OrcamentoService orcamentoService,
        AgenciaService agenciaService) {
        super(service);
        this.orcamentoService = orcamentoService;
        this.agenciaService = agenciaService;
    }

    @Override
    public String pageTitle() {
        return Msg.get("contrato");
    }

    public List<OrcamentoDTO> completeOrcamento(String texto) {
        return orcamentoService.findByTexto(texto);
    }

    @Override
    public void novo() {
        super.novo();
        editForm.setDataDocumento(java.time.LocalDate.now());
        editForm.setMoeda(Moeda.BRL);
        editForm.setRegras(carregarRegrasPadrao());
        orcamentoSelecionado = null;
        dataConsultaPrecos = null;
        preReservaValidaAte = null;
        viajanteForm = new ViajanteDTO();
    }

    public void selecionarOrcamento() {
        if (orcamentoSelecionado == null) {
            return;
        }
        editForm.setCliente(orcamentoSelecionado.getCliente());
        editForm.setConsultor(orcamentoSelecionado.getConsultor());
        editForm.setOrigem(orcamentoSelecionado.getOrigem());
        editForm.setDestino(orcamentoSelecionado.getDestino());
        editForm.setInicio(orcamentoSelecionado.getInicio());
        editForm.setFim(orcamentoSelecionado.getFim());
        editForm.setMoeda(orcamentoSelecionado.getMoeda());
        editForm.setViajantes(new ArrayList<>(orcamentoSelecionado.getViajantes()));
        editForm.setTrajetosAereos(orcamentoSelecionado.getTrajetosAereos());
        editForm.setTrajetosRodoviarios(orcamentoSelecionado.getTrajetosRodoviarios());
        editForm.setHospedagens(orcamentoSelecionado.getHospedagens());
        editForm.setTransfers(orcamentoSelecionado.getTransfers());
        editForm.setAlugueis(orcamentoSelecionado.getAlugueis());
        editForm.setSegurosViagem(orcamentoSelecionado.getSegurosViagem());
        editForm.setESims(orcamentoSelecionado.getESims());
        editForm.setObservacoes(orcamentoSelecionado.getObservacoes());
        editForm.setDataDocumento(LocalDate.now());
    }

    @Override
    public void save() {
        if (orcamentoSelecionado == null || orcamentoSelecionado.getStatus() != StatusOrcamento.CLIENTE_INTERESSADO) {
            addErrorMessage("contrato.orcamento.status.invalido");
            return;
        }
        ZoneId fuso = obterFusoHorario();
        editForm.setOrcamento(orcamentoSelecionado);
        editForm.setDataConsultaPrecos(paraInstant(dataConsultaPrecos, fuso));
        editForm.setPreReservaValidaAte(paraInstant(preReservaValidaAte, fuso));
        editForm = service.salvar(editForm);
        atualizarResultados();
        voltarPesquisa();
        addInfoMessage("entidade.salva", pageTitle());
    }

    @Override
    public void edit(Long id) {
        throw new UnsupportedOperationException("Contrato emitido não pode ser editado.");
    }

    @Override
    public void delete(Long id) {
        throw new UnsupportedOperationException("Contrato emitido não pode ser excluído.");
    }

    public void gerarPdf(Long id) throws IOException, JRException {
        byte[] pdf = service.gerarPdf(id, Map.of());
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().responseReset();
        context.getExternalContext().setResponseContentType("application/pdf");
        context.getExternalContext().setResponseHeader("Content-Disposition", "attachment; filename=contrato.pdf");
        context.getExternalContext().setResponseContentLength(pdf.length);
        context.getExternalContext().getResponseOutputStream().write(pdf);
        context.responseComplete();
    }

    public StatusContrato[] getStatuses() {
        return StatusContrato.values();
    }

    public TipoDocumentoViajante[] getTiposDocumentoViajante() {
        return TipoDocumentoViajante.values();
    }

    public void novoViajante() {
        viajanteForm = new ViajanteDTO();
    }

    public void adicionarViajante() {
        editForm.getViajantes().add(viajanteForm);
    }

    public void removerViajante(int indice) {
        if (indice >= 0 && indice < editForm.getViajantes().size()) {
            editForm.getViajantes().remove(indice);
        }
    }

    public ViajanteDTO getViajanteForm() {
        return viajanteForm;
    }

    public void setViajanteForm(ViajanteDTO viajanteForm) {
        this.viajanteForm = viajanteForm;
    }

    public LocalDateTime getDataConsultaPrecos() {
        return dataConsultaPrecos;
    }

    public void setDataConsultaPrecos(LocalDateTime dataConsultaPrecos) {
        this.dataConsultaPrecos = dataConsultaPrecos;
    }

    public LocalDateTime getPreReservaValidaAte() {
        return preReservaValidaAte;
    }

    public void setPreReservaValidaAte(LocalDateTime preReservaValidaAte) {
        this.preReservaValidaAte = preReservaValidaAte;
    }

    public OrcamentoDTO getOrcamentoSelecionado() {
        return orcamentoSelecionado;
    }

    public void setOrcamentoSelecionado(OrcamentoDTO orcamentoSelecionado) {
        this.orcamentoSelecionado = orcamentoSelecionado;
    }

    private ZoneId obterFusoHorario() {
        String fuso = agenciaService.configuracao().getCidade().getFusoHorario();
        if (fuso == null || fuso.isBlank()) {
            throw new IllegalStateException("Configure a cidade e o fuso horário da agência antes de criar contratos.");
        }
        return ZoneId.of(fuso);
    }

    private Instant paraInstant(LocalDateTime dataHora, ZoneId fuso) {
        return dataHora == null ? null : dataHora.atZone(fuso).toInstant();
    }

    private String carregarRegrasPadrao() {
        try (var leitor = new java.io.BufferedReader(
            new java.io.InputStreamReader(getClass().getResourceAsStream("/reports/contrato-clausulas.txt"),
                java.nio.charset.StandardCharsets.UTF_8))) {
            return leitor.lines().filter(linha -> !linha.isBlank()).map(linha -> {
                String[] partes = linha.split("\\|", 3);
                return partes.length == 3 ? partes[2].trim() : "";
            }).filter(texto -> !texto.isBlank()).collect(java.util.stream.Collectors.joining("\n\n"));
        } catch (IOException e) {
            throw new IllegalStateException("Não foi possível carregar as cláusulas padrão do contrato.", e);
        }
    }
}
