package pt.rikmartins.utilitarios.noticias;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public abstract class SitioNoticias {
    private List<Noticia> noticias;
    private URL enderecoAlternativo;

    private static Document obterPagina(URL endereco, boolean forcarActualizacao) {
        Document pagina;
        try {
            pagina = Jsoup.connect(endereco.toExternalForm()).get();
        } catch (IOException e) {
            e.printStackTrace();
            pagina = null;
        }
        return pagina;
    }

    public SitioNoticias() {
        enderecoAlternativo = null;
    }

    public SitioNoticias(URL enderecoAlternativo) {
        this.enderecoAlternativo = enderecoAlternativo;
    }

    /**
     * Função chamada após obtenção da página, deve implementar o preenchimento
     * do atributo <code>noticias</code>.
     *
     * @param pagina <code>Document</code> que contém a totalidade da página obtida
     *               da internet
     */
    protected abstract ClasseEElemento[] processarSitioNoticias(Document pagina);

    public URL getEndereco() {
        return enderecoAlternativo != null ? enderecoAlternativo : getEnderecoOriginal();
    }

    public URL getEnderecoAlternativo() {
        return enderecoAlternativo;
    }

    public abstract URL getEnderecoOriginal();

    public boolean actualizarNoticias(boolean forcarActualizacao) {
        if (estaPreenchido() && !forcarActualizacao) return false;

        Document pagina = obterPagina(getEndereco(), forcarActualizacao);
        if (pagina == null) return false;

        ClasseEElemento[] elementosNoticia = processarSitioNoticias(pagina);
        if (elementosNoticia == null) return false;

        List<Noticia> noticias = new ArrayList<Noticia>();
        for (ClasseEElemento classeEElemento : elementosNoticia) {
            if (classeEElemento == null) break;

            try {
                adicionarNoticia(classeEElemento.classeNoticia.getConstructor(Noticia.class, URL.class).newInstance(classeEElemento.elementoNoticia, getEndereco()));
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
                return false;
            }
        }

        this.noticias = noticias;
        return true;
    }

    public final boolean actualizarNoticias() {
        return actualizarNoticias(false);
    }

    public final void adicionarNoticia(Noticia noticia) {
        noticias.add(noticia);
    }

    public final void removerNoticia(Noticia noticia) {
        noticias.remove(noticia);
    }

    public final List<Noticia> getNoticias() {
        return noticias;
    }

    public final boolean estaPreenchido() {
        return !(noticias == null || noticias.isEmpty());
    }

    protected final static class ClasseEElemento {
        public Class<? extends Noticia> classeNoticia;
        public Element elementoNoticia;

        public ClasseEElemento(Class<? extends Noticia> classeNoticia, Element elementoNoticia) {
            this.classeNoticia = classeNoticia;
            this.elementoNoticia = elementoNoticia;
        }
    }

    public abstract static class Noticia {
        protected Boolean valida = false;

        protected SitioNoticias sitioNoticias;

        protected String titulo;
        protected String subtitulo;
        protected String texto;
        protected URL enderecoNoticia;
        protected URL enderecoImagem;
        protected byte[] imagem;
        protected Set<String> etiquetas;

        public Noticia(Element elemento, SitioNoticias sitioNoticias) {
            this.sitioNoticias = sitioNoticias;
            coisocoiso(elemento);
            this.valida = true;
            this.etiquetas = new HashSet<>();
        }

        private static byte[] obterImagem(URL endereco) throws IOException {
            try (InputStream in = new BufferedInputStream(endereco.openStream());
                 ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                byte[] buf = new byte[1024];
                int n = 0;
                while (-1 != (n = in.read(buf))) out.write(buf, 0, n);
                return out.toByteArray();
            }
        }

        private void coisocoiso(Element elemento){
            preparaNoticia(elemento);
            if (this.enderecoImagem != null)
                try {
                    this.imagem = obterImagem(enderecoImagem);
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        protected abstract void preparaNoticia(Element elemento);

        public final Boolean getValida() {
            return this.valida;
        }

        public final SitioNoticias getSitioNoticias() {
            return this.sitioNoticias;
        }

        public final String getTitulo() {
            return this.titulo;
        }

        public final String getSubtitulo() {
            return this.subtitulo;
        }

        public final URL getEnderecoNoticia() {
            return this.enderecoNoticia;
        }

        public final URL getEnderecoImagem() {
            return this.enderecoImagem;
        }

        public final String getTexto() {
            return this.texto;
        }

        public final Set<String> getEtiquetas() {
            return etiquetas;
        }
    }
}
