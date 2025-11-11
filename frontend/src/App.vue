
<template>
  <div id="app">
    <h1>Transactions App</h1>

    <div v-if="transactions.length">
      <p>
        Total: {{ getTotal }}
      </p>
      <table class="table">
        <thead>
        <tr>
          <th scope="col">#</th>
          <th scope="col">Name</th>
          <th scope="col">Amount</th>
          <th scope="col">Update</th>
          <th scope="col">Delete</th>
        </tr>
        </thead>
        <tbody>
        <tr :key="transaction.id" v-for="transaction in transactions">
          <th scope="row">{{ transaction.id }}</th>
          <td><input class="form-control mb-2" v-model="transaction.name"></td>
          <td><input class="form-control mb-2" v-model="transaction.amount"></td>
          <td><button class="btn btn-primary mb-2" @click="updateTransaction(transaction)">Update</button></td>
          <td><button class="btn btn-primary mb-2" @click="deleteTransaction(transaction)">Delete</button></td>
        </tr>
        </tbody>
      </table>
    </div>
    <br>

    <p :class="resultMessageClass">{{ resultMessage }}</p>
    <form style="padding-left: 400px; padding-right: 400px">
      <div class="form-row align-items-center">
        <div class="col-auto">
          <label class="sr-only" for="name">Name</label>
          <input class="form-control mb-2" id="name" v-model="inputTransactionName" type="text">
        </div>
        <div class="col-auto">
          <label class="sr-only" for="amount">Amount</label>
          <input class="form-control mb-2" id="amount" v-model="inputTransactionAmount" type="text">
        </div>
        <div class="col-auto">
          <button class="btn btn-primary mb-2" @click="addTransaction">Add transaction</button>
        </div>
      </div>
    </form>

    <button class="btn btn-primary mb-2" @click="deleteTransactions">Delete transactions</button>
  </div>
</template>

<script>
import axios from 'axios';
const api = axios.create({
  // withCredentials: true
});
api.defaults.timeout = 500;

export default {
  data() {
    return {
      transactions: [],
      inputTransactionId: '',
      inputTransactionName: '',
      inputTransactionAmount: '',
      resultMessage: '',
      resultMessageClass: {
        "red-text": false,
        "green-text": false
      },
    }
  },
  created() {
    this.initTransactions();
  },
  computed: {
    getTotal() {
      return this.transactions.reduce((sum, item) => sum + Number(item.amount), 0);
    }
  },
  methods: {
    async initTransactions() {
      const result = await api.get("http://localhost:8080/api/transactions");
      this.transactions = result.data;
    },
    async addTransaction() {
      const result = await api.post("http://localhost:8080/api/transaction", {
        name: this.inputTransactionName,
        amount: this.inputTransactionAmount,
      }).catch(error => {
        if (error.name === 'CanceledError') {
          console.log('Запрос отменён');
        } else {
          console.error('Ошибка запроса:', error);
        }
      });
      if (result.data.success) {
        this.inputTransactionName = '';
        this.inputTransactionAmount = '';
      }
      this.showMessage(result.data);
      await this.initTransactions();
    },
    async deleteTransactions() {
      const result = await api.delete("http://localhost:8080/api/transactions");
      this.showMessage(result.data);
      await this.initTransactions();
    },
    async updateTransaction(transaction) {
      const result = await api.put("http://localhost:8080/api/transaction/" + transaction.id, {
        name: transaction.name,
        amount: transaction.amount,
      }).catch(function (error) {
        console.log(error.toJSON());
      });
      console.log(result);
      this.showMessage(result.data);
      this.initTransactions();
    },
    async deleteTransaction(transaction) {
      const result = await api.delete("http://localhost:8080/api/transaction/" + transaction.id);
      this.showMessage(result.data);
      this.initTransactions();
    },
    showMessage(resultMessage) {
      console.log(resultMessage);
      if (resultMessage.success) {
        this.resultMessageClass["green-text"] = true;
        this.resultMessageClass["red-text"] = false;
      } else {
        this.resultMessageClass["green-text"] = false;
        this.resultMessageClass["red-text"] = true;
      }
      this.resultMessage = resultMessage.message;
    }
  }
};
</script>

<style>
#app {
  font-family: Arial, sans-serif;
  text-align: center;
}
.red-text {
  color: red;
}
.green-text {
  color: green;
}
</style>