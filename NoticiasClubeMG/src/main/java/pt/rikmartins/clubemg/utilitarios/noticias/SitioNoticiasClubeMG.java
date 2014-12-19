package pt.rikmartins.clubemg.utilitarios.noticias;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import pt.rikmartins.utilitarios.noticias.SitioNoticias;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Created by ricardo on 01-12-2014.
 */
public class SitioNoticiasClubeMG extends SitioNoticias {
    public SitioNoticiasClubeMG() {
        super();
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
        protected URL enderecoImagemGrande;

        public NoticiaClubeMG(Element elemento, SitioNoticias sitioNoticias) {
            super(elemento, sitioNoticias);
        }

//            <div class="post-1504 post type-post hentry category-btt tag-guarda tag-serra-da-estrela featured">
//                <div class="postwrap ui-widget ui-widget-content thumb">
//                    <h3 class="title"><a href="http://www.montanhismo-guarda.pt/portal/?p=1504">Invernal de BTT Cidade da Guarda 2014</a></h3>
//                    <div class="clear"></div>
//                    <div class="thumbwrap">
//                        <div class="itx-featured">
//                        <div class="ui-widget-header cont"><a href="http://www.montanhismo-guarda.pt/portal/?p=1504"><img src="http://www.montanhismo-guarda.pt/portal/wp-content/uploads/2014/10/Invernal2014_CMG_V1_Medium-300x200.jpg" alt="Invernal de BTT Cidade da Guarda 2014" height="200"></a></div>				</div>
//                    </div>
//                    <div class="entry">
//                        <p>Está aí mais uma edição da Invernal de BTT. Mais informações em www.montanhismo-guarda.pt/invernalbtt e em www.facebook.com/invernalbtt</p>
//                    </div>
//                    <div class="clear"></div>
//                </div>
//            </div>
//            --------------------------------------------------------------------
//            <div class="post-1513 post type-post hentry category-montanha tag-serra-da-estrela tag-travessias-de-montanha posts postwrap-1">
//                <div class="postwrap ui-widget ui-widget-content thumb">
//                    <h3 class="title"><a href="http://www.montanhismo-guarda.pt/portal/?p=1513">Travessia dos Três Cântaros</a></h3>
//                    <div class="clear"></div>
//                    <div class="thumbwrap">
//                        <div class="itx-thumb">
//                        <div class="ui-widget-header cont"><a href="http://www.montanhismo-guarda.pt/portal/?p=1513"><img src="http://www.montanhismo-guarda.pt/portal/wp-content/uploads/2014/10/BaseTravessiaInvernal_2014-75x100.jpg" alt="Travessia dos Três Cântaros" height="100"></a></div>				</div>
//                    </div>
//                    <div class="entry">
//                        <p>Encontram-se abertas as&nbsp;inscrições&nbsp;para a&nbsp; Travessia Invernal em Autonomia 2014 – “Travessia dos&nbsp;Três Cântaros”,&nbsp;a realizar no dia&nbsp;16 de Novembro,&nbsp;na Serra da <a href="http://www.montanhismo-guarda.pt/portal/?p=1513"><b>… Ler mais</b></a></p>
//                    </div>
//                    <div class="clear"></div>
//                </div>
//            </div>
        @Override
        protected void extraiNoticia(Element elemento) {
            Set<String> classes = elemento.classNames();
            for (String classe : classes) {
                StringTokenizer tokenizer = new StringTokenizer(classe, "-");
                String cabeca = tokenizer.nextToken();
                StringBuilder corpoBuilder = new StringBuilder();
                while (tokenizer.hasMoreTokens()) {
                    corpoBuilder.append(tokenizer.nextToken());
                    if (tokenizer.hasMoreTokens())
                        corpoBuilder.append(" ");
                    else
                        break;
                }
                String corpo = corpoBuilder.toString();

                if (cabeca.equals("category")) this.categoria = corpo;
                else if (cabeca.equals("tag")) this.etiquetas.add(corpo);
                else if (cabeca.equals("featured")) this.destacada = true;
                else if (cabeca.equals("post") && !corpo.equals("")) this.identificacaoNoticia = corpo;
            }

            Element postwrap = elemento.child(0);

            Element title = postwrap.getElementsByClass("title").first();
            this.titulo = title.text();
            this.subtitulo = null;
            this.texto = postwrap.getElementsByClass("entry").first().text();
            try {
                this.enderecoNoticia = new URL(title.getElementsByTag("a").attr("href"));
            } catch (MalformedURLException e) {
                e.printStackTrace(); // TODO: Fazer algo com esta excepção, e tentar dar a volta com URI concatenado ao url do sítio
            }

            String enderecoImagemHtml = postwrap.getElementsByClass("thumbwrap").first().getElementsByTag("img").attr("src");

            try {
                this.enderecoImagemGrande = new URL(enderecoImagemHtml.replaceAll("-\\d+x\\d+.", "."));
            } catch (MalformedURLException e) {
                e.printStackTrace(); // TODO: Fazer algo com esta excepção, e tentar dar a volta com URI concatenado ao url do sítio
            }

            try {
                this.enderecoImagem = new URL(enderecoImagemHtml);
            } catch (MalformedURLException e){
                e.printStackTrace(); // TODO: Fazer algo com esta excepção, e tentar dar a volta com URI concatenado ao url do sítio
            }
        }

        public URL getEnderecoImagemGrande() {
            return enderecoImagemGrande;
        }
    }
}
