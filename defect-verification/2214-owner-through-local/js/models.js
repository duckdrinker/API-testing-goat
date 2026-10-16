const { Sequelize, DataTypes } = require('sequelize');

const sequelize = new Sequelize('sqlite::memory:');

const Order = sequelize.define('Order', {
  owner: DataTypes.STRING,
  status: DataTypes.STRING,
});

module.exports = { sequelize, Order };
