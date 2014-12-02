package pt.rikmartins.utilitarios.noticias;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public abstract class SitioNoticias {
	private List<Noticia> noticias;

	/**
	 * Função chamada após obtenção da página, deve implementar o preenchimento
	 * do atributo <code>noticias</code>.
	 *
	 * @param pagina
	 *            <code>Document</code> que contém a totalidade da página obtida
	 *            da internet
	 */
	protected abstract ClasseEElemento[] processarSitioNoticias(Document pagina);

	public abstract URL getEndereco();

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
}
