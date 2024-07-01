import config from "./config.js";

// Pega o corpo (tbody) da tabela principal de exibição de cursos no html (tr_principal)
const coursesList = document.getElementById('courses');

// Pega a tabela principal para exibir cursos no html
const tr_principal = document.getElementById("tr-principal");

// Pega a opção de cursos ativos no html
const option_active = document.getElementById("option-active");

// Pega a opção de cursos inativos no html
const option_inative = document.getElementById("options-inative-container");

// Guarda o id do curso clicado quando um click é executado em um dos ícones na tabela (Compras, vendas, edit ou delete)
var courseClicked 


// Faz com que "tr-principal" e "courses" sejam limpos para adição de novos dados
function removeChild(){
    while (coursesList.firstChild) {
        coursesList.removeChild(coursesList.firstChild);
    }
    while (tr_principal.firstChild) {
        tr_principal.removeChild(tr_principal.firstChild);
    }
}

// Muda o texto interto e a visibidade das opções de "Ativos / Inativos" para "Voltar"
function changeOptions(){
    option_active.textContent = "Ativos"
    option_inative.style.display = "grid"
}

// Cria a estrutura básica para mostrar os cursos na página
function createStructureCursos(){
    
    const thId = document.createElement("th");
    const thName = document.createElement("th");
    const thCategory = document.createElement("th");
    const thDescription = document.createElement("th");
    const thPrice = document.createElement("th");
    const thAuthor = document.createElement("th");

    thId.textContent = "Id";
    thName.textContent = "Nome";
    thCategory.textContent = "Categoria";
    thDescription.textContent = "Descrição"
    thPrice.textContent = "Preço"
    thAuthor.textContent = "Autor"
    tr_principal.appendChild(thId);
    tr_principal.appendChild(thName);
    tr_principal.appendChild(thDescription);
    tr_principal.appendChild(thCategory);
    tr_principal.appendChild(thPrice);
    tr_principal.appendChild(thAuthor);
}

// Cria cada coluna e seta cada curso nela
function setCourse(course, tr){
    const tdId = document.createElement('td');
    tdId.textContent = course.id;
    tr.appendChild(tdId);

    // Coluna com o nome do curso
    const tdName = document.createElement('td');
    tdName.textContent = course.name;
    tr.appendChild(tdName);

    // Coluna com a descrição do curso
    const tdDescription = document.createElement('td');
    tdDescription.textContent = course.description;
    tr.appendChild(tdDescription);

    // Coluna com a categoria do curso
    const tdCategory = document.createElement('td');
    tdCategory.textContent = course.category;
    tr.appendChild(tdCategory);

    // Coluna com o preço do curso
    const tdPrice = document.createElement('td');
    tdPrice.textContent = course.price;
    tr.appendChild(tdPrice);

    // Coluna com o autor do curso
    const tdAutor = document.createElement('td');
    tdAutor.textContent = course.author.id;
    tr.appendChild(tdAutor);
}

// Cria o ícone de editar
function createEditIcon(tdActions, course){
    const editLink = document.createElement('a');
    editLink.href = '#editEmployeeModal';
    editLink.classList.add('edit');
    editLink.dataset.toggle = 'modal';
    editLink.addEventListener('click', () => setCourseClicked(course.id)); // Adiciona evento de clique
    const editIcon = document.createElement('i');
    editIcon.classList.add('material-icons');
    editIcon.dataset.toggle = 'tooltip';
    editIcon.title = 'Edit';
    editIcon.textContent = '\u{E254}'; // Unicode para o ícone
    editLink.appendChild(editIcon);
    tdActions.appendChild(editLink);
}

//Cria o ícone de deletar
function createDeleteIcon(tdActions, course){
    const deleteLink = document.createElement('a');
    deleteLink.href = '#deleteEmployeeModal';
    deleteLink.classList.add('delete');
    deleteLink.dataset.toggle = 'modal';
    deleteLink.addEventListener('click', () => setCourseClicked(course.id)); // Adiciona evento de clique
    const deleteIcon = document.createElement('i');
    deleteIcon.classList.add('material-icons');
    deleteIcon.dataset.toggle = 'tooltip';
    deleteIcon.title = 'Delete';
    deleteIcon.textContent = '\u{E872}'; // Unicode para o ícone
    deleteLink.appendChild(deleteIcon);
    tdActions.appendChild(deleteLink);
}

// Cria o ícone de compras
function createBuyIcon(tdActions, course){
    const buyLink = document.createElement('a');
    buyLink.href = '#buyEmployeeModal';
    buyLink.classList.add('buy');
    buyLink.dataset.toggle = 'modal';
    buyLink.addEventListener('click', () => loadPurchases(course.id)); // Adiciona evento de clique
    const buyIcon = document.createElement('i');
    buyIcon.classList.add('material-icons');
    buyIcon.dataset.toggle = 'tooltip';
    buyIcon.title = 'Buy';
    buyIcon.textContent = '\u{E8CC}'; // Unicode para o ícone de compras
    buyLink.appendChild(buyIcon);
    tdActions.appendChild(buyLink);
}

// Cria o ícone de vendas
function createSellIcon(tdActions, course){
    const sellLink = document.createElement('a');
    sellLink.href = '#sellEmployeeModal';
    sellLink.classList.add('sell');
    sellLink.dataset.toggle = 'modal';
    sellLink.addEventListener('click', () => loadSales(course.id)); // Adiciona evento de clique
    const sellIcon = document.createElement('i');
    sellIcon.classList.add('material-icons');
    sellIcon.dataset.toggle = 'tooltip';
    sellIcon.title = 'Sell';
    sellIcon.textContent = '\u{E227}'; // Unicode para o ícone de vendas
    sellLink.appendChild(sellIcon);
    tdActions.appendChild(sellLink);
}

// Cria o ícone para reativar um curso
function createReactivationIcon(tdActions, course) {
    const reactivationLink = document.createElement('a');
    reactivationLink.href = '#reactivateCourseModal';
    reactivationLink.classList.add('reactivate');
    reactivationLink.dataset.toggle = 'modal';
    reactivationLink.addEventListener('click', () => reactivateCourse(course.id)); // Adiciona evento de clique
    const reactivationIcon = document.createElement('i');
    reactivationIcon.classList.add('material-icons');
    reactivationIcon.dataset.toggle = 'tooltip';
    reactivationIcon.title = 'Reativar';
    reactivationIcon.textContent = '\u{E5D5}'; // Unicode para o ícone de "refresh"
    reactivationLink.appendChild(reactivationIcon);
    tdActions.appendChild(reactivationLink);
}

function createInativeIcons(tdActions, course){
    createReactivationIcon(tdActions, course)
}


// Cria a estrutura de ícones para cursos
function createIconsCourses(tdActions, course){
    createEditIcon(tdActions, course);
    createDeleteIcon(tdActions, course);
    createBuyIcon(tdActions, course);
    createSellIcon(tdActions, course);
}

// Cria a lógica de exibição de usuários
function loadCoursesLogic(course){
    const tr = document.createElement('tr');
    const tdActions = document.createElement('td');
    setCourse(course, tr);
    createIconsCourses(tdActions, course);
    tr.appendChild(tdActions);
    coursesList.appendChild(tr);
}

// Faz o request para o backend pegando todos os cursos
function loadCourses() {
    removeChild()
    changeOptions()
    createStructureCursos()

    fetch(`${config.baseURL}/courses`)
        .then(response => response.json())
        .then(data => {
            data.filter(course => course.active === true).forEach(course =>{
                loadCoursesLogic(course)
            });
        })
        .catch(error => {
            alert(error.message)
        });
}

// Carrega os cursos inativos (deletados)
function loadCoursesInativeLogic(course){
    const tr = document.createElement('tr');
    const tdActions = document.createElement('td');
    setCourse(course, tr);
    createInativeIcons(tdActions, course)
    tr.appendChild(tdActions);
    coursesList.appendChild(tr);
}

// Carrega os cursos ativos (Não deletados) 
function loadCoursesInative() {
    removeChild()
    changeOptions()
    createStructureCursos();

    fetch(`${config.baseURL}/courses`)
        .then(response => response.json())
        .then(data => {
            data.filter(course => course.active === false).forEach(course =>{
                loadCoursesInativeLogic(course);
            });
        })
        .catch(error => {
            alert(error.message)
        });
}

// Adicionando as duas funções para o contexto do documento
document.addEventListener('DOMContentLoaded', function() {
    document.querySelector('div[onclick="loadCoursesInative()"]').addEventListener('click', loadCoursesInative);
});

document.addEventListener('DOMContentLoaded', function() {
    document.querySelector('div[onclick="loadCourses()"]').addEventListener('click', loadCourses);
});


function changeOptionsButtons(){
    option_active.textContent = "Voltar"
    option_inative.style.display = "none"
}


// Retorna uma mensagem quando um curso não possui vendas
function noSales(){
    const h3 = document.createElement("h3");
    h3.textContent = "Não há vendas";
    tr_principal.appendChild(h3);
}

// Cria a estrutura da tabela de vendas
function createSalesStructure(){
    const thId = document.createElement("th");
    const thDate = document.createElement("th");
    const thValue = document.createElement("th");
    const thSeller = document.createElement("th");
    thDate.textContent = "Data";
    thValue.textContent = "Valor";
    thId.textContent = "Id";
    thSeller.textContent = "Vendedor";
    tr_principal.appendChild(thId);
    tr_principal.appendChild(thDate);
    tr_principal.appendChild(thValue);
    tr_principal.appendChild(thSeller);
}

// Seta as vendas de um curso na tabela
function setSales(sale, tr){
    // Coluna com o ID da venda
    const tdId = document.createElement('td');
    tdId.textContent = sale.id;
    tr.appendChild(tdId);

    // Coluna com o timestamp da venda
    const tdTimestamp = document.createElement('td');
    
    // Criar um objeto Date
    const date = new Date(sale.timestamp[2], sale.timestamp[1], sale.timestamp[0], sale.timestamp[3], sale.timestamp[4], sale.timestamp[5]);
    tdTimestamp.textContent = date;
    tr.appendChild(tdTimestamp);

    // Coluna com o valor da venda
    const tdValue = document.createElement('td');
    tdValue.textContent = sale.value;
    tr.appendChild(tdValue);

    // Coluna com o id do vendedor
    const tdVendedor = document.createElement('td');
    tdVendedor.textContent = sale.seller.id;
    tr.appendChild(tdVendedor);
}

// Implementa a lógica de exibição das vendas
function loadSalesLogic(sale){
    // Criando a estrutura HTML para cada venda
    const tr = document.createElement('tr');

    setSales(sale, tr)
    
    // Coluna com os ícones de ação (editar e deletar)
    const tdActions = document.createElement('td');
    // Adicione seus ícones de ação aqui, se necessário
    tr.appendChild(tdActions);

    // Adicionando a linha (tr) à lista de cursos
    coursesList.appendChild(tr);
}

// Faz o request para o back pegando as vendas de um curso
function loadSales(courseId) {
    removeChild()
    fetch(`${config.baseURL}/sales/course/${courseId}`)
        .then(response => response.json())
        .then(sales => {
            if (!sales || sales.length === 0) {
                noSales();
            } else {       
            createSalesStructure();
            sales.forEach(sale => {
                loadSalesLogic(sale)
            });
        }
        changeOptionsButtons()
    })
        .catch(error => {
            alert(error.message)
        });
}

// Retorna uma mensagem quando um curso não possui compras
function noPurchases(){
    const h3 = document.createElement("h3");
    h3.textContent = "Não há compras";
    tr_principal.appendChild(h3);
}

// Cria a estrutura da tabela de compras
function createPurchasesStructure(){
    const thId = document.createElement("th");
    const thDate = document.createElement("th");
    const thValue = document.createElement("th");
    const thBuyer = document.createElement("th");
    thDate.textContent = "Data";
    thValue.textContent = "Valor";
    thId.textContent = "Id";
    thBuyer.textContent = "Comprador";
    tr_principal.appendChild(thId);
    tr_principal.appendChild(thDate);
    tr_principal.appendChild(thValue);
    tr_principal.appendChild(thBuyer);
}

// Seta as compras de um curso
function setPurchases(purchase, tr){

    // Coluna com o ID da compra
    const tdId = document.createElement('td');
    tdId.textContent = purchase.id;
    tr.appendChild(tdId);

    // Coluna com o timestamp da compra
    const tdTimestamp = document.createElement('td');
    
    // Criar um objeto Date
    const date = new Date(purchase.timestamp[2], purchase.timestamp[1], purchase.timestamp[0], purchase.timestamp[3], purchase.timestamp[4], purchase.timestamp[5]);
    tdTimestamp.textContent = date;
    tr.appendChild(tdTimestamp);

    // Coluna com o valor da compra
    const tdValue = document.createElement('td');
    tdValue.textContent = purchase.value;
    tr.appendChild(tdValue);

    // Coluna com o id do comprador
    const tdBuyer = document.createElement('td');
    tdBuyer.textContent = purchase.buyer.id;
    tr.appendChild(tdBuyer);


}

// Cria a lógica do carregamento de compras
function loadPurchasesLogic(purchase){
    const tr = document.createElement('tr');
    setPurchases(purchase, tr)

    const tdActions = document.createElement('td');
    tr.appendChild(tdActions);
    coursesList.appendChild(tr);
}

// Faz o request de compras de um curso para o endpoint
function loadPurchases(courseId) {
    removeChild()
    fetch(`${config.baseURL}/purchases/course/${courseId}`)
        .then(response => response.json())
        .then(purchases => {
            if (!purchases || purchases.length === 0) {
                noPurchases();
            } else {       
            createPurchasesStructure();    
            purchases.forEach(purchase => {
                loadPurchasesLogic(purchase);
            });
        }
        changeOptionsButtons();
    })
        .catch(error => {
            alert(error.message)
        });
}

// Faz um request para o back para reativar um curso
function reactivateCourse(courseId){
    fetch(`${config.baseURL}/courses/recover/${courseId}`, {
        method: 'PUT'
    }).then(response => {
        if (response.ok){
            location.reload();
        } else {
            return response.json().then(error => {
                alert(error.message || "Erro desconhecido");
            })
        }
    }).catch(error => {
        console.error('Erro ao deletar usuário:', error);
    });
}

function setCourseClicked(courseId){
    courseClicked = courseId
}

// Função para deletar curso
function deleteCourse() {
    // alert(`Será excutado na URL: ${config.baseURL}/courses/${courseClicked}`)
    fetch(`${config.baseURL}/courses/${courseClicked}`, {
        method: 'DELETE'
    }).then(response => {
        if (response.ok){
            location.reload();
        } else {
            return response.json().then(error => {
                alert(error.message || "Erro desconhecido");
            })
        }
    }).catch(error => {
        console.error('Erro ao deletar curso:', error);
    });
}

// Carrega os cursos quando a página "/courses" é iniciada.
document.addEventListener('DOMContentLoaded', function() {
    loadCourses();
});

// Adicionando um EventListener para o submit do edit course form
document.getElementById('deleteCourseForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Evita o envio padrão do formulário

    // Chama a função deleteCourse() para deletar o curso clicado
    deleteCourse();
});


// Adicionando um EventListener para o submit do edit course form
document.getElementById('editForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Evita o envio padrão do formulário

    const name = document.getElementById('edit-name').value;
    const description = document.getElementById('edit-description').value;
    const category = document.getElementById('edit-category').value;
    const price = document.getElementById('edit-price').value;

    fetch(`${config.baseURL}/courses/${courseClicked}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        name: name,
        description: description,
        category: category,
        price: price,
      })
    })
    .then(response => {
      if (response.ok){
        location.reload();
      } else {
        return response.json().then(error => {
            console.log(error)
            alert(error.message || "Erro desconhecido");
        })
      }
    })
    .catch((error) => {
      console.log(error);
    });
  });

// Adicionando um EventListener para o submit do create course form
document.getElementById('courseForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Evita o envio padrão do formulário

    const name = document.getElementById('name').value;
    const description = document.getElementById('description').value;
    const category = document.getElementById('category').value;
    const price = document.getElementById('price').value;
    const authorId = document.getElementById('author').value;

    fetch(`${config.baseURL}/courses`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        name: name,
        description: description,
        category: category,
        price: price,
        author: { id: authorId }
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
