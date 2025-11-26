package com.shakalinux.apicardaio.controller;

import com.shakalinux.apicardaio.dtos.PedidoRequestDTO;
import com.shakalinux.apicardaio.dtos.PedidoResponseDTO;
import com.shakalinux.apicardaio.exception.BadRequestException;
import com.shakalinux.apicardaio.model.Pedido;
import com.shakalinux.apicardaio.model.enums.StatusPedido;
import com.shakalinux.apicardaio.service.PedidoService;
import com.shakalinux.apicardaio.utils.PedidoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement; // Importante
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pedidos")
@Tag(name = "Pedidos", description = "Gestão de pedidos: criação por clientes e administração por operadores.")
public class PedidoController {
    @Autowired
    private PedidoService pedidoService;

    @Operation(
            summary = "Criar um novo pedido",
            description = "Endpoint público para clientes. Cria um novo pedido com os itens e informações de entrega fornecidas."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação (ex: pedido vazio, dados de item inválidos)")
    })
    @PostMapping
    public ResponseEntity<PedidoResponseDTO> criar(@RequestBody PedidoRequestDTO dto) {

        if (dto.getItens() == null || dto.getItens().isEmpty()) {
            throw new BadRequestException("O pedido deve conter pelo menos 1 item.");
        }

        Pedido pedido = pedidoService.criarPedido(dto);

        PedidoResponseDTO resp = PedidoMapper.toResponse(pedido);

        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }



    @Operation(
            summary = "Buscar detalhes do pedido por ID",
            description = "Consulta os detalhes de um pedido. Pode ser usado por clientes (apenas para seus pedidos) ou por administradores."

    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado para o ID fornecido")

    })
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> buscar(@PathVariable Long id) {

        Pedido pedido = pedidoService.buscarPorId(id);

        return ResponseEntity.ok(PedidoMapper.toResponse(pedido));
    }



    @Operation(
            summary = "Alterar o status de um pedido",
            description = "Atualiza o status do pedido (Ex: PENDENTE, EM_PREPARACAO, ENVIADO, ENTREGUE). **Requer permissão de ADMIN.**"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status do pedido alterado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Status inválido fornecido"),
            @ApiResponse(responseCode = "401", description = "Não autorizado (Token ausente ou inválido)"),
            @ApiResponse(responseCode = "403", description = "Proibido (Usuário não tem permissão de ADMIN)"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    @SecurityRequirement(name = "BearerAuth")
    @PutMapping("/{id}/status")
    public ResponseEntity<?> alterarStatus(@PathVariable Long id, @RequestParam String status) {

        Pedido pedido = pedidoService.buscarPorId(id);

        try {
            pedido.setStatus(StatusPedido.valueOf(status.toUpperCase())); // Normalizando para enum
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Status inválido. Status permitidos: " + StatusPedido.values());
        }

        pedidoService.salvar(pedido);

        return ResponseEntity.ok(Map.of(
                "mensagem", "Status do pedido atualizado para " + status
        ));
    }


    @Operation(
            summary = "Listar todos os pedidos no sistema",
            description = "Retorna a lista completa de todos os pedidos. Ideal para o Painel Administrativo. **Requer permissão de ADMIN.**"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de pedidos retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado (Token ausente ou inválido)"),
            @ApiResponse(responseCode = "403", description = "Proibido (Usuário não tem permissão de ADMIN)")
    })
    @SecurityRequirement(name = "BearerAuth")
    @GetMapping
    public ResponseEntity<List<PedidoResponseDTO>> listarTodos() {

        List<Pedido> pedidos = pedidoService.listarTodos();

        List<PedidoResponseDTO> lista = pedidos.stream()
                .map(PedidoMapper::toResponse)
                .toList();

        return ResponseEntity.ok(lista);
    }

}