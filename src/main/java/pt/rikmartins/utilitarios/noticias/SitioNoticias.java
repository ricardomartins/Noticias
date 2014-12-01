package pt.rikmartins.utilitarios.noticias;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;

public abstract class SitioNoticias {
	private Map<String, List<Noticia>> asCategorias;
	private String categoriaActiva;

	/**
	 * Função chamada após obtenção da página, deve implementar o preenchimento
	 * do atributo <code>asCategorias</code>.
	 * 
	 * @param pagina
	 *            <code>Document</code> que contém a totalidade da página obtida
	 *            da internet
	 */
	public abstract void processarSitioNoticias(Document pagina);

	public abstract URL getEndereco(); 

	public final SitioNoticias adicionarCategoria(String nomeCategoria) {
		List<Noticia> aNovaLista = new ArrayList<Noticia>();
		asCategorias.put(nomeCategoria, aNovaLista);

		categoriaActiva = nomeCategoria;
		return this;
	}

	public final SitioNoticias renomearCategoria(String nomeCategoria,
			String novoNome) {
		List<Noticia> aLista = asCategorias.get(nomeCategoria);
		asCategorias.put(novoNome, aLista);

		categoriaActiva = novoNome;
		return this;
	}

	public final SitioNoticias renomearCategoria(String novoNome) {
		return renomearCategoria(categoriaActiva, novoNome);
	}

	public final SitioNoticias removerCategoria(String categoria) {
		asCategorias.remove(categoria);

		return this;
	}

	public final SitioNoticias adicionarNoticia(String categoria,
			Noticia noticia) {
		if(!asCategorias.containsKey(categoria)){
			adicionarCategoria(categoria);
		}
		asCategorias.get(categoria).add(noticia);

		categoriaActiva = categoria;
		return this;
	}

	public final SitioNoticias adicionarNoticia(Noticia noticia) {
		return adicionarNoticia(categoriaActiva, noticia);
	}

	public final SitioNoticias removerNoticia(String categoria, Noticia noticia) {
		asCategorias.get(categoria).remove(noticia);

		categoriaActiva = categoria;
		return this;
	}

	public final SitioNoticias removerNoticia(Noticia noticia) {
		return removerNoticia(categoriaActiva, noticia);
	}

	public final SitioNoticias removerNoticia(String categoria,
			int indiceNoticia) {
		asCategorias.get(categoria).remove(indiceNoticia);

		categoriaActiva = categoria;
		return this;
	}

	public final SitioNoticias removerNoticia(int indiceNoticia) {
		return removerNoticia(categoriaActiva, indiceNoticia);
	}

	public final List<Noticia> getListaNoticias(String categoria) {
		return asCategorias.get(categoria);
	}

	public final boolean estaPreenchido() {
		if (asCategorias == null || asCategorias.isEmpty()) {
			return false;
		}
		for (List<Noticia> lNoticias : asCategorias.values()) {
			if (!lNoticias.isEmpty()) {
				return true;
			}
		}
		return false;
	}
}
