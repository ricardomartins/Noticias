package pt.rikmartins.utilitarios.noticias;

import org.jsoup.nodes.Element;

public abstract class Noticia {
	protected Boolean valida = false;

	protected String enderecoDoSitio = "";

	protected String original = "";
	protected String titulo = ""; 
	protected String subtitulo = "";
	protected String texto = "";
	protected String enderecoNoticia = "";  // endereço relativo para já
	protected String enderecoImagem = "";  // endereço relativo para já
	
	public Noticia() {
	}

	public Noticia(Element html) {
		preparaNoticia(html);
		this.valida = true;
	}

	public Noticia(Element html, String enderecoGlobal) {
		this.enderecoDoSitio = enderecoGlobal;
		preparaNoticia(html);
		this.valida = true;

	}

	public abstract Noticia preparaNoticia(Element html);

	// Obtensores directos
	public String obterOriginal() {
		return this.original;
	}

	public Boolean obterValida() {
		return this.valida;
	}

	public String obterEnderecoDoSitio() {
		return this.enderecoDoSitio;
	}

	public String obterTitulo() {
		return this.titulo;
	}

	public String obterSubtitulo() {
		return this.subtitulo;
	}

	public String obterEnderecoNoticia() {
		return this.enderecoNoticia;
	}

	public String obterEnderecoImagem() {
		return this.enderecoImagem;
	}

	public String obterTexto() {
		return this.texto;
	}

}
