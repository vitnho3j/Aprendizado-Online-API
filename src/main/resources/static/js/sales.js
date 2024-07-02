import config from "./config.js";

// Pega o corpo (tbody) da tabela principal de exibição de vendas no html (tr_principal)
const salesList = document.getElementById('sales');

// Cria cada coluna e seta cada venda nela
function setSales(sale, tr){

    // Coluna com o id da venda
    const tdId = document.createElement('td');
    tdId.textContent = sale.id;
    tr.appendChild(tdId);
    
    const tdTimestamp = document.createElement('td');
    tdTimestamp.textContent = sale.timestamp;
    tr.appendChild(tdTimestamp);

    // Coluna com o valor da venda
    const tdValue = document.createElement('td');
    tdValue.textContent = sale.value;
    tr.appendChild(tdValue);

    // Coluna com o id do vendedor
    const tdSeller = document.createElement('td');
    tdSeller.textContent = sale.seller.id;
    tr.appendChild(tdSeller);

    // Coluna com o id do curso da venda
    const tdCourse = document.createElement('td');
    tdCourse.textContent = sale.course.id;
    tr.appendChild(tdCourse);
}

// Cria a lógica de exibição de vendas
function loadSalesLogic(sale){
    const tr = document.createElement('tr');
    const tdActions = document.createElement('td');
    setSales(sale, tr);
    tr.appendChild(tdActions);
    salesList.appendChild(tr);
}

// Faz o request para o backend pegando todas as vendas
function loadSales() {
    fetch(`${config.baseURL}/sales`)
        .then(response => response.json())
        .then(data => {
            data.forEach(sale =>{
                loadSalesLogic(sale)
            });
        })
        .catch(error => {
            alert(error.message)
        });
}

// Carrega as vendas quando a página "/vendas" é iniciada.
document.addEventListener('DOMContentLoaded', function() {
    loadSales();
});

document.getElementById('saleForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Evita o envio padrão do formulário

    const idSeller = document.getElementById('id_seller').value;
    const idCourse = document.getElementById('id_course').value;


    fetch(`${config.baseURL}/sales`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        seller:{
            id:idSeller
        },
        course:{
            id:idCourse
        }
      })
    })
    .then(response => {
      if (response.ok){
        location.reload();
      } else {
        return response.json().then(error => {
            alert(error.message || "Erro desconhecido");
            console.log(error)
        })
      }
    })
    .catch((error) => {
      console.log(error);
    });
  });




