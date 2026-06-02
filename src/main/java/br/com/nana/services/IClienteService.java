/**
 * 
 */
package br.com.nana.services;

import br.com.nana.domain.Cliente;
import br.com.nana.exceptions.DAOException;
import br.com.nana.exceptions.TipoChaveNaoEncontradaException;
import br.com.nana.services.generic.IGenericService;

/**
 * @author anderson.salviano
 *
 */
public interface IClienteService extends IGenericService<Cliente, Long> {

//	Boolean cadastrar(Cliente cliente) throws TipoChaveNaoEncontradaException;
//
	Cliente buscarPorCPF(Long cpf) throws DAOException;
//
//	void excluir(Long cpf);
//
//	void alterar(Cliente cliente) throws TipoChaveNaoEncontradaException;

}
