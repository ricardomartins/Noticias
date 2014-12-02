package pt.rikmartins.utilitarios.noticias;

import org.jsoup.nodes.Element;

import java.net.URI;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public abstract class Noticia {
	protected Boolean valida = false;

	protected URL enderecoDoSitio;

	protected String titulo;
	protected String subtitulo;
	protected String texto;
	protected URI enderecoNoticia;  // endereço relativo para já
	protected URI enderecoImagem;  // endereço relativo para já
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
