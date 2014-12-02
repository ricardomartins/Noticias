package pt.rikmartins.clubemg.utilitarios.noticias;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import pt.rikmartins.utilitarios.noticias.SitioNoticias;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ricardo on 01-12-2014.
 */
public class SitioNoticiasClubeMG extends SitioNoticias{
    public SitioNoticiasClubeMG() {
    }

    public SitioNoticiasClubeMG(URL enderecoAlternativo) {
        super(enderecoAlternativo);
    }

    @Override
    protected ClasseEElemento[] processarSitioNoticias(Document pagina) {
        Element contentwrap = pagina.getElementById("contentwrap");
        Elements noticias = contentwrap.getElementsByClass("post");

        ClasseEElemento[] resultado = new ClasseEElemento[noticias.size()];
        int n = 0;
        for (Element element : noticias) {
            resultado[n] = new ClasseEElemento(NoticiaClubeMG.class, element);
            n++;
        }
        return resultado;
    }

    @Override
    public URL getEnderecoOriginal() {
        try {
            return new URL("http://www.montanhismo-guarda.pt/portal/");
        } catch (MalformedURLException e) {
            assert false;
        }
        return null; // Inatingível
    }

    public static class NoticiaClubeMG extends Noticia {
        public NoticiaClubeMG(Element elemento, SitioNoticias sitioNoticias) {
            super(elemento, sitioNoticias);
        }

        @Override
        protected void preparaNoticia(Element elemento) {
        }
    }

}
