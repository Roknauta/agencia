package com.roknauta.agencia.web.jasper.documento.comum;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.roknauta.agencia.dto.documento.FotoHospedagemDTO;
import com.roknauta.agencia.dto.documento.HospedagemDTO;

class HospedagemRelatorioDTOTest {

    @Test
    void deveAgruparFotosDuasPorLinhaEManterUltimaFotoSemPar() throws Exception {
        FotoHospedagemDTO primeira = foto((byte) 1);
        FotoHospedagemDTO segunda = foto((byte) 2);
        FotoHospedagemDTO terceira = foto((byte) 3);
        HospedagemDTO hospedagem = new HospedagemDTO();
        hospedagem.setFotos(List.of(primeira, segunda, terceira));

        HospedagemRelatorioDTO relatorio = new HospedagemRelatorioDTO(hospedagem);

        assertEquals(2, relatorio.getFotos().size());
        assertEquals(2, relatorio.getParesFotos().size());
        assertArrayEquals(new byte[] {1}, ler(relatorio.getParesFotos().get(0).getEsquerda()));
        assertArrayEquals(new byte[] {2}, ler(relatorio.getParesFotos().get(0).getDireita()));
        assertArrayEquals(new byte[] {3}, ler(relatorio.getParesFotos().get(1).getEsquerda()));
        assertNull(relatorio.getParesFotos().get(1).getDireita());
    }

    @Test
    void deveAceitarHospedagemSemFotos() {
        HospedagemRelatorioDTO relatorio = new HospedagemRelatorioDTO(new HospedagemDTO());

        assertNotNull(relatorio.getFotos());
        assertNotNull(relatorio.getParesFotos());
        assertEquals(0, relatorio.getFotos().size());
        assertEquals(0, relatorio.getParesFotos().size());
    }

    private static FotoHospedagemDTO foto(byte conteudo) {
        FotoHospedagemDTO foto = new FotoHospedagemDTO();
        foto.setConteudo(new byte[] {conteudo});
        return foto;
    }

    private static byte[] ler(InputStream input) throws Exception {
        assertNotNull(input);
        try (input; ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            input.transferTo(output);
            return output.toByteArray();
        }
    }
}
