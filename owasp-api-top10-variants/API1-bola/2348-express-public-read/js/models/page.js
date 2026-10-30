const { DataTypes } = require('sequelize')
const sequelize = require('./db')

// Public entity: pages are readable by anyone
module.exports = sequelize.define('Page', { title: DataTypes.STRING, body: DataTypes.TEXT })