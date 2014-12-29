package pt.rikmartins.utilitarios.noticias;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class SitioNoticias {
    private List<Noticia> noticias;
    private URL           enderecoAlternativo;
    private Set<String>   etiquetas;
    private Set<String>   categorias;

    public SitioNoticias() {
        this(null);
    }

    public SitioNoticias(URL enderecoAlternativo) {
        this.enderecoAlternativo = enderecoAlternativo;
        this.noticias = new ArrayList<Noticia>();
        this.etiquetas = new HashSet<String>();
        this.categorias = new HashSet<String>();
    }

    private static Document obterPagina(URL endereco) throws IOException {
        return Jsoup.connect(endereco.toExternalForm()).get();
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

    public boolean actualizarNoticias() throws IOException {
        Document pagina = obterPagina(getEndereco());
        if (pagina == null) return false;

        ClasseEElemento[] elementosNoticia = processarSitioNoticias(pagina);
        if (elementosNoticia == null) return false;

        List<Noticia> noticias = new ArrayList<Noticia>();
        for (ClasseEElemento classeEElemento : elementosNoticia) {
            if (classeEElemento == null) return false;

            try {
                noticias.add(classeEElemento.classeNoticia.getConstructor(Element.class, SitioNoticias.class)
                                                          .newInstance(classeEElemento.elementoNoticia, this));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                return false;
            } catch (InvocationTargetException e) {
                return false;
            } catch (InstantiationException e) {
                return false;
            } catch (IllegalAccessException e) {
                return false;
            }
        }

        for (Noticia noticia : noticias)
            adicionarNoticia(noticia);
        return true;
    }

    protected final void adicionarNoticia(Noticia noticia) {
        this.noticias.add(noticia);
        this.etiquetas.addAll(noticia.etiquetas);
        this.categorias.add(noticia.categoria);
    }

    public final void removerNoticia(Noticia noticia) {
        noticias.remove(noticia);
    }

    public final List<Noticia> getNoticias() {
        return noticias;
    }

    public Set<String> getEtiquetas() {
        return etiquetas;
    }

    public Set<String> getCategorias() {
        return categorias;
    }

    public final boolean estaPreenchido() {
        return !(noticias == null || noticias.isEmpty());
    }

    protected final static class ClasseEElemento {
        public Class<? extends Noticia> classeNoticia;
        public Element                  elementoNoticia;

        public ClasseEElemento(Class<? extends Noticia> classeNoticia, Element elementoNoticia) {
            this.classeNoticia = classeNoticia;
            this.elementoNoticia = elementoNoticia;
        }
    }

    public abstract static class Noticia {
        protected SitioNoticias sitioNoticias;

        protected String      identificacaoNoticia;
        protected String      titulo;
        protected String      subtitulo;
        protected String      texto;
        protected URL         enderecoNoticia;
        protected URL         enderecoImagem;
        protected Set<String> etiquetas;
        protected String      categoria;
        protected boolean     destacada;

        public Noticia(Element elemento, SitioNoticias sitioNoticias) {
            this.sitioNoticias = sitioNoticias;
            this.etiquetas = new HashSet<String>();
            this.destacada = false;
            preparaNoticia(elemento);
        }

        private void preparaNoticia(Element elemento) {
            extraiNoticia(elemento);
        }

        protected abstract void extraiNoticia(Element elemento);

        public final String getIdentificacaoNoticia() {
            return identificacaoNoticia;
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

        public final String getTexto() {
            return this.texto;
        }

        public final URL getEnderecoNoticia() {
            return this.enderecoNoticia;
        }

        public final URL getEnderecoImagem() {
            return this.enderecoImagem;
        }

        public final Set<String> getEtiquetas() {
            return etiquetas;
        }

        public final String getCategoria() {
            return categoria;
        }

        public final boolean isDestacada() {
            return destacada;
        }
    }
}
