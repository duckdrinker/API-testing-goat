const { DataTypes } = require('sequelize')
const sequelize = require('./db')

const Page = sequelize.define('Page', { title: DataTypes.STRING })
module.exports = Page
