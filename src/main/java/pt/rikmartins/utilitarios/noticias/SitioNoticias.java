package pt.rikmartins.utilitarios.noticias;

import java.io.IOException;
import java.net.URI;
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
	 * @param pagina
	 *            <code>Document</code> que contém a totalidade da página obtida
	 *            da internet
	 */
	protected abstract ClasseEElemento[] processarSitioNoticias(Document pagina);

	public URL getEndereco(){
		return enderecoAlternativo != null ? enderecoAlternativo : getEnderecoOriginal();
	}

	public URL getEnderecoAlternativo(){
		return enderecoAlternativo;
	}

	public abstract URL getEnderecoOriginal();

	public boolean actualizarNoticias(boolean forcarActualizacao){
		if (estaPreenchido() && !forcarActualizacao) return false;

		Document pagina = obterPagina(forcarActualizacao);
		if (pagina == null) return false;

		ClasseEElemento[] elementosNoticia = processarSitioNoticias(pagina);
		if (elementosNoticia == null) return false;

		List<Noticia> noticias = new ArrayList<Noticia>();
		for (ClasseEElemento classeEElemento : elementosNoticia){
			if (classeEElemento == null) break;

			try {
				this.noticias.add(classeEElemento.classeNoticia.getConstructor(Noticia.class, URL.class).newInstance(classeEElemento.elementoNoticia, getEndereco()));
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

	private Document obterPagina(boolean forcarActualizacao) {
		Document pagina;
		try {
			pagina = Jsoup.connect(getEndereco().toExternalForm()).get();
		} catch (IOException e) {
			e.printStackTrace();
			pagina = null;
		}
		return pagina;
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

	protected static class ClasseEElemento {
		public Class<? extends Noticia> classeNoticia;
		public Element elementoNoticia;
		public ClasseEElemento(Class<? extends Noticia> classeNoticia, Element elementoNoticia){
			this.classeNoticia = classeNoticia;
			this.elementoNoticia = elementoNoticia;
		}
	}

	public abstract static class Noticia {
        protected Boolean valida = false;

        protected URL enderecoDoSitio;

        protected String titulo;
        protected String subtitulo;
        protected String texto;
        protected URI enderecoNoticia;  // endereço relativo para já
        protected URI enderecoImagem;  // endereço relativo para já
		protected byte[] imagem;
        protected Set<String> etiquetas;

        public Noticia(Element html, URL enderecoGlobal) {
            this.enderecoDoSitio = enderecoGlobal;
            preparaNoticia(html);
            this.valida = true;
            this.etiquetas = new HashSet<String>();
        }

        protected abstract Noticia preparaNoticia(Element html);

        public Boolean getValida() {
            return this.valida;
        }

        public URL getEnderecoDoSitio() {
            return this.enderecoDoSitio;
        }

        public String getTitulo() {
            return this.titulo;
        }

        public String getSubtitulo() {
            return this.subtitulo;
        }

        public URI getEnderecoNoticia() {
            return this.enderecoNoticia;
        }

        public URI getEnderecoImagem() {
            return this.enderecoImagem;
        }

        public String getTexto() {
            return this.texto;
        }

        public Set<String> getEtiquetas() {
            return etiquetas;
        }
    }
}
