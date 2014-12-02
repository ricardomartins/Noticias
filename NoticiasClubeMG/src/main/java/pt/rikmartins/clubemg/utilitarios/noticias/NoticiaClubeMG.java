package pt.rikmartins.clubemg.utilitarios.noticias;

import org.jsoup.nodes.Element;
import pt.rikmartins.utilitarios.noticias.Noticia;

import java.net.URL;

/**
 * Created by ricardo on 01-12-2014.
 */
public class NoticiaClubeMG extends Noticia{
    public NoticiaClubeMG(Element html, URL enderecoGlobal) {
        super(html, enderecoGlobal);
    }

    @Override
    protected Noticia preparaNoticia(Element html) {
        return null;
    }
}
