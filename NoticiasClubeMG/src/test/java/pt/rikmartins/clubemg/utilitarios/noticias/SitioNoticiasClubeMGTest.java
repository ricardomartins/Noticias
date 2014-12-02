package pt.rikmartins.clubemg.utilitarios.noticias;

import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.*;

public class SitioNoticiasClubeMGTest {
    @Test
    public void testGetEndereco() throws Exception {
        SitioNoticiasClubeMG sitioNoticiasClubeMG1 = new SitioNoticiasClubeMG();
        assertEquals(sitioNoticiasClubeMG1.getEndereco().toString(), "http://www.montanhismo-guarda.pt/portal/");
        SitioNoticiasClubeMG sitioNoticiasClubeMG2 = new SitioNoticiasClubeMG(new URL("http://www.montanhismo-guarda.pt/"));
        assertEquals(sitioNoticiasClubeMG2.getEndereco().toString(), "http://www.montanhismo-guarda.pt/");
    }

    @Test
    public void testGetEnderecoOriginal() throws Exception {
        SitioNoticiasClubeMG sitioNoticiasClubeMG1 = new SitioNoticiasClubeMG();
        assertEquals(sitioNoticiasClubeMG1.getEnderecoOriginal().toString(), "http://www.montanhismo-guarda.pt/portal/");
        SitioNoticiasClubeMG sitioNoticiasClubeMG2 = new SitioNoticiasClubeMG(new URL("http://www.montanhismo-guarda.pt/"));
        assertEquals(sitioNoticiasClubeMG2.getEnderecoOriginal().toString(), "http://www.montanhismo-guarda.pt/portal/");
    }

    @Test
    public void testGetEnderecoAlternativo() throws Exception {
        SitioNoticiasClubeMG sitioNoticiasClubeMG1 = new SitioNoticiasClubeMG();
        assertNull(sitioNoticiasClubeMG1.getEnderecoAlternativo());
        SitioNoticiasClubeMG sitioNoticiasClubeMG2 = new SitioNoticiasClubeMG(new URL("http://www.montanhismo-guarda.pt/"));
        assertEquals(sitioNoticiasClubeMG2.getEnderecoAlternativo().toString(), "http://www.montanhismo-guarda.pt/");
    }

    @Test
    public void testActualizarNoticias() throws Exception {
        SitioNoticiasClubeMG sitioNoticiasClubeMG1 = new SitioNoticiasClubeMG();
        sitioNoticiasClubeMG1.actualizarNoticias();
    }

    @Test
    public void testGetNoticias() throws Exception {

    }

    @Test
    public void testEstaPreenchido() throws Exception {

    }
}