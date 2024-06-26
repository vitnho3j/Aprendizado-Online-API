import config from "./config.js";

// Pega o corpo (tbody) da tabela principal de exibição de compras no html (tr_principal)
const purchasesList = document.getElementById('purchases');

// Cria cada coluna e seta cada compra nela
function setPurchases(purchase, tr){

    // Coluna com o id da compra
    const tdId = document.createElement('td');
    tdId.textContent = purchase.id;
    tr.appendChild(tdId);
    
    const tdTimestamp = document.createElement('td');
    tdTimestamp.textContent = purchase.timestamp;
    tr.appendChild(tdTimestamp);

    // Coluna com o valor da compra
    const tdValue = document.createElement('td');
    tdValue.textContent = purchase.value;
    tr.appendChild(tdValue);

    // Coluna com o id do comprador
    const tdBuyer = document.createElement('td');
    tdBuyer.textContent = purchase.buyer.id;
    tr.appendChild(tdBuyer);

    // Coluna com o id do curso da compra
    const tdCourse = document.createElement('td');
    tdCourse.textContent = purchase.course.id;
    tr.appendChild(tdCourse);
}

// Cria a lógica de exibição de compras
function loadPurchasesLogic(purchase){
    const tr = document.createElement('tr');
    const tdActions = document.createElement('td');
    setPurchases(purchase, tr);
    tr.appendChild(tdActions);
    purchasesList.appendChild(tr);
}

// Faz o request para o backend pegando todas as compras
function loadPurchases() {
    fetch(`${config.baseURL}/purchases`)
        .then(response => response.json())
        .then(data => {
            data.forEach(purchase =>{
                loadPurchasesLogic(purchase)
            });
        })
        .catch(error => {
            alert(error.message)
        });
}

// Carrega as compras quando a página "/purchases" é iniciada.
document.addEventListener('DOMContentLoaded', function() {
    loadPurchases();
});

document.getElementById('purchaseForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Evita o envio padrão do formulário

    const idBuyer = document.getElementById('id_buyer').value;
    const idCourse = document.getElementById('id_course').value;


    fetch(`${config.baseURL}/purchases`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        buyer:{
            id:idBuyer
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




