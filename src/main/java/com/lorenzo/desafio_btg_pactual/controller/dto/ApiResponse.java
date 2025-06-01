package com.lorenzo.desafio_btg_pactual.controller.dto;


//Valor total do pedido
//Quantidade de Pedidos por Cliente
//Lista de pedidos realizados por cliente

import java.util.List;
import java.util.Map;

public record ApiResponse<T>(Map<String,Object> summary,
                             List<T> data,
                             PaginationResponse pagination) {

}
