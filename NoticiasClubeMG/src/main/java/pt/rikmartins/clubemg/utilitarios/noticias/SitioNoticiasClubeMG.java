package pt.rikmartins.clubemg.utilitarios.noticias;

import org.jsoup.nodes.Document;
import pt.rikmartins.utilitarios.noticias.SitioNoticias;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ricardo on 01-12-2014.
 */
public class SitioNoticiasClubeMG extends SitioNoticias{
    @Override
    protected ClasseEElemento[] processarSitioNoticias(Document pagina) {
        return new ClasseEElemento[0];
    }

    @Override
    public URL getEndereco() {
        try {
            return new URL("http://www.montanhismo-guarda.pt/portal/");
        } catch (MalformedURLException e) {
            assert false;
        }
        return null; // Inatingível
    }
}
