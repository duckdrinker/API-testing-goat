const { DataTypes } = require('sequelize')
const sequelize = require('./db')


module.exports = sequelize.define('Page', { title: DataTypes.STRING })
