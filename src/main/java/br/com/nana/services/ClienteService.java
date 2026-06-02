/**
 * 
 */
package br.com.nana.services;

import br.com.nana.dao.IClienteDAO;
import br.com.nana.domain.Cliente;
import br.com.nana.exceptions.DAOException;
import br.com.nana.exceptions.MaisDeUmRegistroException;
import br.com.nana.exceptions.TableException;
import br.com.nana.exceptions.TipoChaveNaoEncontradaException;
import br.com.nana.services.generic.GenericService;

/**
 * @author anderson.salviano
 *
 */
public class ClienteService extends GenericService<Cliente, Long> implements IClienteService {
	
	//private IClienteDAO clienteDAO;
	
	public ClienteService(IClienteDAO clienteDAO) {
		super(clienteDAO);
		//this.clienteDAO = clienteDAO;
	}

//	@Override
//	public Boolean salvar(Cliente cliente) throws TipoChaveNaoEncontradaException {
//		return clienteDAO.cadastrar(cliente);
//	}

	@Override
	public Cliente buscarPorCPF(Long cpf) throws DAOException {
		try {
			return this.dao.consultar(cpf);
		} catch (MaisDeUmRegistroException | TableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

//	@Override
//	public void excluir(Long cpf) {
//		clienteDAO.excluir(cpf);
//	}
//
//	@Override
//	public void alterar(Cliente cliente) throws TipoChaveNaoEncontradaException{
//		clienteDAO.alterar(cliente);
//	}

}
