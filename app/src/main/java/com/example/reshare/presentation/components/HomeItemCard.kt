package com.example.reshare.presentation.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.reshare.R
import com.example.reshare.domain.model.Product
import com.example.reshare.presentation.utils.Screen
import com.example.reshare.presentation.utils.capitalizeFirst
import com.example.reshare.presentation.utils.getBadgeStyle
import com.example.reshare.ui.theme.BlueD
import com.example.reshare.ui.theme.DarkPurple
import com.example.reshare.ui.theme.LightPurple
import com.example.reshare.ui.theme.MilkM
import com.example.reshare.ui.theme.OrangeM
import com.example.reshare.ui.theme.YellowD

@SuppressLint("DefaultLocale")
@Composable
fun HomeItemCard(
    modifier: Modifier = Modifier,
    product: Product,
    navController: NavController
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val cardWidth = screenWidth / 2

    Card(
        modifier = Modifier
            .width(cardWidth)
            .clickable {
                navController.currentBackStackEntry?.savedStateHandle?.set("product", product)
                navController.navigate(Screen.ItemDetail.route)
            },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            Box {
                AsyncImage(
                    model = product.images[0],
                    contentDescription = "Product Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.img),
                    error = painterResource(R.drawable.img)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Badge(product.tag, product.originalPrice)
                    Box {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.StarOutline,
                                contentDescription = "Favorite",
                                tint = Color.White,
                                modifier = Modifier
                                    .size(20.dp)
                                    .background(Color.Black.copy(alpha = 0.3f), shape = CircleShape)
                            )
                        }
                        LocationTag(
                            distance = String.format("%.1f km", product.distance / 1000.0),
                            modifier = Modifier
                                .offset(y = 92.dp)
                        )
                    }
                }

            }

            Text(
                text = product.title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .padding(top = 10.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (product.tag.lowercase() == "reduced" && product.originalPrice != null && product.discountPercent != null) {
                ReducedPriceSection(
                    originalPrice = product.originalPrice,
                    discountPercent = product.discountPercent,
                    discountedPrice = product.originalPrice * (1 - product.discountPercent / 100.0)
                )
            }
            UserInfo(product.createdBy.firstName, product.createdBy.profilePic)
        }
    }
}

@Composable
fun Badge(tag: String, originalPrice: Double? = null) {
    val style = getBadgeStyle(tag)

    Box(
        modifier = Modifier
            .background(style.backgroundColor, shape = CircleShape)
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        if(tag == "paid")
            Text("%.1fk".format(originalPrice),
                color = style.textColor, fontSize = 12.sp, style = TextStyle(lineHeight = 12.sp))
        else
            Text(tag.capitalizeFirst(), color = style.textColor, fontSize = 12.sp, style = TextStyle(lineHeight = 12.sp))
    }
}

@Composable
fun LocationTag(distance: String, modifier: Modifier = Modifier) {

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(start = 4.dp, end = 6.dp, top = 2.dp, bottom = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.LocationOn,
                contentDescription = null,
                tint = DarkPurple,
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = distance,
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                color = Color.Black,
                style = TextStyle(lineHeight = 12.sp)
            )
        }
    }
}

@Composable
fun UserInfo(name: String, avatar: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(8.dp)
    ) {
        AsyncImage(
            model = avatar,
            contentDescription = "User Avatar",
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.user),
            error = painterResource(R.drawable.user)
        )
        Text(
            text = name, fontSize = 14.sp,
            modifier = Modifier.padding(start = 8.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun ReducedPriceSection(
    originalPrice: Double,
    discountPercent: Int,
    discountedPrice: Double
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "%.1fk".format(originalPrice),
            fontSize = 12.sp,
            color = Color.Gray,
            textDecoration = TextDecoration.LineThrough
        )
        Text(
            text = "%.1fk".format(discountedPrice),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text(
            text = "-$discountPercent%",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF009688)
        )
    }
}

