package com.estimulo.logistics.logisticsInfo.applicationService;

import java.util.ArrayList;
import java.util.HashMap;

import com.estimulo.logistics.logisticsInfo.dao.ItemDAO;
import com.estimulo.logistics.logisticsInfo.to.ItemInfoTO;
import com.estimulo.logistics.logisticsInfo.to.ItemTO;
import com.estimulo.logistics.material.dao.BomDAO;
import com.estimulo.logistics.material.to.BomTO;
import com.estimulo.system.base.dao.CodeDetailDAO;
import com.estimulo.system.base.to.CodeDetailTO;

public class ItemApplicationServiceImpl implements ItemApplicationService {

	private ItemDAO itemDAO;
	
	public void setItemDAO(ItemDAO itemDAO) {
		this.itemDAO=itemDAO;
	}
	private CodeDetailDAO codeDetailDAO;
	
	public void setCodeDetailDAO(CodeDetailDAO codeDetailDAO) {
		this.codeDetailDAO=codeDetailDAO;
	}
	
	private BomDAO bomDAO;
	public void setBomDAO(BomDAO bomDAO) {
		this.bomDAO=bomDAO;
	}

	public ArrayList<ItemInfoTO> getItemInfoList(String searchCondition, String[] paramArray) {

		ArrayList<ItemInfoTO> itemInfoList = null;


			switch (searchCondition) {

			case "ALL":

				itemInfoList = itemDAO.selectAllItemList();

				break;

			case "ITEM_CLASSIFICATION":

				itemInfoList = itemDAO.selectItemList("ITEM_CLASSIFICATION", paramArray);

				break;

			case "ITEM_GROUP_CODE":

				itemInfoList = itemDAO.selectItemList("ITEM_GROUP_CODE", paramArray);

				break;

			case "STANDARD_UNIT_PRICE":

				itemInfoList = itemDAO.selectItemList("STANDARD_UNIT_PRICE", paramArray);

				break;

			}

		return itemInfoList;
	}

	public HashMap<String, Object> batchItemListProcess(ArrayList<ItemTO> itemTOList) {


		HashMap<String, Object> resultMap = new HashMap<>();

			ArrayList<String> insertList = new ArrayList<>();
			ArrayList<String> updateList = new ArrayList<>();
			ArrayList<String> deleteList = new ArrayList<>();

			CodeDetailTO detailCodeTO = new CodeDetailTO();
			BomTO bomTO = new BomTO();
			
			for (ItemTO TO : itemTOList) {

				String status = TO.getStatus();

				switch (status) {

				case "INSERT":

					itemDAO.insertItem(TO);
					insertList.add(TO.getItemCode());

					// CODE_DETAIL �뀒�씠釉붿뿉 Insert
					detailCodeTO.setDivisionCodeNo(TO.getItemClassification());
					detailCodeTO.setDetailCode(TO.getItemCode());
					detailCodeTO.setDetailCodeName(TO.getItemName());
					detailCodeTO.setDescription(TO.getDescription());

					codeDetailDAO.insertDetailCode(detailCodeTO);

					
					// �깉濡쒖슫 �뭹紐⑹씠 �셿�젣�뭹 (ItemClassification : "IT-CI") , 諛섏젣�뭹 (ItemClassification : "IT-SI") �씪 寃쎌슦 BOM �뀒�씠釉붿뿉 Insert
					if( TO.getItemClassification().equals("IT-CI") || TO.getItemClassification().equals("IT-SI") ) {
						
						bomTO.setNo(1);
						bomTO.setParentItemCode("NULL");
						bomTO.setItemCode( TO.getItemCode() );
						bomTO.setNetAmount(1);
						
						bomDAO.insertBom(bomTO);
					}
					
					
					break;

				case "UPDATE":

					itemDAO.updateItem(TO);

					updateList.add(TO.getItemCode());

					// CODE_DETAIL �뀒�씠釉붿뿉 Update
					detailCodeTO.setDivisionCodeNo(TO.getItemClassification());
					detailCodeTO.setDetailCode(TO.getItemCode());
					detailCodeTO.setDetailCodeName(TO.getItemName());
					detailCodeTO.setDescription(TO.getDescription());

					codeDetailDAO.updateDetailCode(detailCodeTO);

					break;

				case "DELETE":

					itemDAO.deleteItem(TO);

					deleteList.add(TO.getItemCode());

					// CODE_DETAIL �뀒�씠釉붿뿉 Delete
					detailCodeTO.setDivisionCodeNo(TO.getItemClassification());
					detailCodeTO.setDetailCode(TO.getItemCode());
					detailCodeTO.setDetailCodeName(TO.getItemName());
					detailCodeTO.setDescription(TO.getDescription());

					codeDetailDAO.deleteDetailCode(detailCodeTO);

					break;

				}

			}

			resultMap.put("INSERT", insertList);
			resultMap.put("UPDATE", updateList);
			resultMap.put("DELETE", deleteList);

		return resultMap;

	}

	@Override
	public int getStandardUnitPrice(String itemCode) {

		int price = 0;

			price = itemDAO.getStandardUnitPrice(itemCode);

		return price;
	}
	
	@Override
	public int getStandardUnitPriceBox(String itemCode) {

		int price = 0;
			
			price = itemDAO.getStandardUnitPriceBox(itemCode);

		return price;
	}
}
