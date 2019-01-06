 CREATE VIEW vw_PrevisaoContas AS
      (select 'R' as Tipo,R.CodigoId,R.Emissao,R.Titulo,R.Vencimento
      ,R.Valor
      ,ROUND(COALESCE(R.AliqMulta,0),2) as AliqMulta 
      ,ROUND(COALESCE(R.AliqJurosDia,0),2) as AliqJurosDia
      ,if(TIMESTAMPDIFF(DAY,R.vencimento,if(R.databaixa is null,CURRENT_DATE(),R.databaixa)) > 0,TIMESTAMPDIFF(DAY,R.vencimento,if(R.databaixa is null,CURRENT_DATE(),R.databaixa)),0) as Atraso 
      ,ROUND(if(TIMESTAMPDIFF(DAY,R.vencimento,if(R.databaixa is null,CURRENT_DATE(),R.databaixa)) > 0,(COALESCE(R.AliqMulta,0)/100)*R.valor,0),2) as ValorMulta 
      ,ROUND(if(TIMESTAMPDIFF(DAY,R.vencimento,if(R.databaixa is null,CURRENT_DATE(),R.databaixa)) > 0,((COALESCE(R.AliqJurosDia,0)/100)*R.valor) * TIMESTAMPDIFF(DAY,R.vencimento,if(R.databaixa is null,CURRENT_DATE(),R.databaixa)) ,0),2) as ValorJuros 
      ,ROUND(R.valor + ROUND(if(TIMESTAMPDIFF(DAY,R.vencimento,if(R.databaixa is null,CURRENT_DATE(),R.databaixa)) > 0,(COALESCE(R.AliqMulta,0)/100)*R.valor,0),2) + ROUND(if(TIMESTAMPDIFF(DAY,R.vencimento,if(R.databaixa is null,CURRENT_DATE(),R.databaixa)) > 0,((COALESCE(R.AliqJurosDia,0)/100)*R.valor) * TIMESTAMPDIFF(DAY,R.vencimento,if(R.databaixa is null,CURRENT_DATE(),R.databaixa)) ,0),2),2) as ValorCorrigido
      ,R.DataBaixa
      ,ROUND(COALESCE(R.valorpago,0),2) as ValorPago
      ,R.codccusto,R.ccusto,R.planoconta as CodigoConta
      ,PL.tipo as TipoPlano,PL.descricao as PlanoContas,R.Devedor as Codigo,R.CNPJ,R.nomedevedor as Nome,R.Telefone
      ,CASE
          WHEN (TIMESTAMPDIFF(DAY,R.vencimento,CURRENT_DATE()) > 0) AND (COALESCE(R.valorpago,0) <= 0) THEN "Atrasado"
          WHEN (R.databaixa is not null) and (COALESCE(R.valorpago,0) <= 0) THEN "Baixado"
          WHEN (R.databaixa is not null) and (R.valorpago > 0) and (TIMESTAMPDIFF(DAY,R.vencimento,R.databaixa) > 0) THEN "Recebido com Atraso"
          WHEN (R.databaixa is not null) and (R.valorpago > 0) and (TIMESTAMPDIFF(DAY,R.vencimento,R.databaixa) = 0) THEN "Recebido em Dia"
          WHEN (R.databaixa is not null) and (R.valorpago > 0) and (TIMESTAMPDIFF(DAY,R.vencimento,R.databaixa) < 0) THEN "Recebido Antecipado"
          WHEN (TIMESTAMPDIFF(DAY,R.vencimento,CURRENT_DATE()) = 0) AND (R.databaixa is null) and (COALESCE(R.valorpago,0) <= 0) THEN "Vencido Hoje"
          ELSE "À Vencer"
      END as Situacao
	  ,R.IdVendedor
	  ,VD.nomevende
      from contasreceber as R
           left outer join planocontas as PL on(R.planoconta = PL.codid)
		   left outer join vendedores as VD on(R.IdVendedor = VD.CodId)
      where R.vencimento >= '1900-01-01' and R.valor > 0)
       UNION
      (select 'P' as Tipo,P.CodigoId,P.Emissao,P.Titulo,P.Vencimento
      ,P.Valor
      ,ROUND(COALESCE(P.AliqMulta,0),2) as AliqMulta
      ,ROUND(COALESCE(P.AliqJurosDia,0),2) as AliqJurosDia
      ,if(TIMESTAMPDIFF(DAY,P.vencimento,if(P.databaixa is null,CURRENT_DATE(),P.databaixa)) > 0,TIMESTAMPDIFF(DAY,P.vencimento,if(P.databaixa is null,CURRENT_DATE(),P.databaixa)),0) as Atraso 
      ,ROUND(if(TIMESTAMPDIFF(DAY,P.vencimento,if(P.databaixa is null,CURRENT_DATE(),P.databaixa)) > 0,(COALESCE(P.AliqMulta,0)/100)*P.valor,0),2) as ValorMulta 
      ,ROUND(if(TIMESTAMPDIFF(DAY,P.vencimento,if(P.databaixa is null,CURRENT_DATE(),P.databaixa)) > 0,((COALESCE(P.AliqJurosDia,0)/100)*P.valor) * TIMESTAMPDIFF(DAY,P.vencimento,if(P.databaixa is null,CURRENT_DATE(),P.databaixa)) ,0),2) as ValorJuros 
      ,ROUND(P.valor + ROUND(if(TIMESTAMPDIFF(DAY,P.vencimento,if(P.databaixa is null,CURRENT_DATE(),P.databaixa)) > 0,(COALESCE(P.AliqMulta,0)/100)*P.valor,0),2) + ROUND(if(TIMESTAMPDIFF(DAY,P.vencimento,if(P.databaixa is null,CURRENT_DATE(),P.databaixa)) > 0,((COALESCE(P.AliqJurosDia,0)/100)*P.valor) * TIMESTAMPDIFF(DAY,P.vencimento,if(P.databaixa is null,CURRENT_DATE(),P.databaixa)) ,0),2),2) as ValorCorrigido
      ,P.DataBaixa
      ,COALESCE(P.valorpago,0) as ValorPago
      ,P.CodcCusto,P.CCusto,P.planoconta as CodigoContas
      ,PL.tipo as tipoplano,PL.descricao as planocontas,P.credor as Codigo,P.CNPJ,P.nomecredor as Nome,P.Telefone 
      ,CASE
          WHEN (TIMESTAMPDIFF(DAY,P.vencimento,CURRENT_DATE()) > 0) AND (COALESCE(P.valorpago,0) <= 0) THEN "Atrasado"
          WHEN (P.databaixa is not null) and (COALESCE(P.valorpago,0) <= 0) THEN "Baixado"
          WHEN (P.databaixa is not null) and (P.valorpago > 0) and (TIMESTAMPDIFF(DAY,P.vencimento,P.databaixa) > 0) THEN "Pago com Atraso"
          WHEN (P.databaixa is not null) and (P.valorpago > 0) and (TIMESTAMPDIFF(DAY,P.vencimento,P.databaixa) = 0) THEN "Pago em Dia"
          WHEN (P.databaixa is not null) and (P.valorpago > 0) and (TIMESTAMPDIFF(DAY,P.vencimento,P.databaixa) < 0) THEN "Pago Antecipado"
          WHEN (TIMESTAMPDIFF(DAY,P.vencimento,CURRENT_DATE()) = 0) AND (P.databaixa is null) and (COALESCE(P.valorpago,0) <= 0) THEN "Vencido Hoje"
          ELSE "À Vencer"
      END as Situacao
	  ,P.IdVendedor
	  ,VD.nomevende
      from contaspagar as P
           left outer join planocontas as PL on(P.planoconta = PL.codid)
		   left outer join vendedores as VD on(P.IdVendedor = VD.CodId)
      where P.vencimento >= '1900-01-01' and P.valor > 0);


