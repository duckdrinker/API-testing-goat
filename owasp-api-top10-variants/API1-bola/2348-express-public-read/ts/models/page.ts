import { DataTypes } from 'sequelize'
import sequelize from './db'

// Public entity: pages are readable by anyone
export const Page = sequelize.define('Page', { title: DataTypes.STRING, body: DataTypes.TEXT })