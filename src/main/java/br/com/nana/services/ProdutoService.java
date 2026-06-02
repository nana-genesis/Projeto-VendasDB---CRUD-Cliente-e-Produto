/**
 * 
 */
package br.com.nana.services;

import br.com.nana.dao.IProdutoDAO;
import br.com.nana.domain.Produto;
import br.com.nana.services.generic.GenericService;

/**
 * @author anderson.salviano
 *
 */
public class ProdutoService extends GenericService<Produto, String> implements IProdutoService {

	public ProdutoService(IProdutoDAO dao) {
		super(dao);
	}

}
