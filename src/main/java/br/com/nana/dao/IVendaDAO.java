/**
 * 
 */
package br.com.nana.dao;

import br.com.nana.dao.generic.IGenericDAO;
import br.com.nana.domain.Venda;
import br.com.nana.exceptions.DAOException;
import br.com.nana.exceptions.TipoChaveNaoEncontradaException;

/**
 * @author anderson.salviano
 *
 */
public interface IVendaDAO extends IGenericDAO<Venda, String> {

	public void finalizarVenda(Venda venda) throws TipoChaveNaoEncontradaException, DAOException;
	
	public void cancelarVenda(Venda venda) throws TipoChaveNaoEncontradaException, DAOException;
}
